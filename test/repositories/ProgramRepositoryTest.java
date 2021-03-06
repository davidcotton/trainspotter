package repositories;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.avaje.ebean.PagedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Channel;
import models.Program;
import models.Program.Status;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class ProgramRepositoryTest extends AbstractIntegrationTest {

  private ProgramRepository programRepository;
  private ChannelRepository channelRepository;
  private ArtistRepository artistRepository;

  public ProgramRepositoryTest() {
    programRepository = new ProgramRepository();
    channelRepository = new ChannelRepository();
    artistRepository = new ArtistRepository();
  }

  @Test public void findAll() throws Exception {
    List<Program> programs = programRepository.findAll();
    assertThat(programs, not(IsEmptyCollection.empty()));
    assertThat(programs.size(), is(5));
    // verify ordering
    assertThat(programs.get(0).getName(), is("Behind The Iron Curtain"));
    assertThat(programs.get(1).getName(), is("Diynamic"));
    assertThat(programs.get(2).getName(), is("House Party"));
    assertThat(programs.get(3).getName(), is("Systematic Session"));
    assertThat(programs.get(4).getName(), is("Transitions"));
  }

  @Test public void findByChannel() throws Exception {
    Channel channel = channelRepository.findById(1).orElseThrow(Exception::new);
    List<Program> programs = programRepository.findByChannel(channel.getId());
    assertThat(programs.size(), is(4));
    // verify ordering
    assertThat(programs.get(0).getName(), is("Behind The Iron Curtain"));
    assertThat(programs.get(1).getName(), is("Diynamic"));
    assertThat(programs.get(2).getName(), is("Systematic Session"));
    assertThat(programs.get(3).getName(), is("Transitions"));
  }

  @Test public void findAllPaged() throws Exception {
    PagedList<Program> pagedPrograms = programRepository.findAllPaged(1);

    // verify the pagination object attributes
    assertThat(pagedPrograms.getPageSize(), is(12));
    assertThat(pagedPrograms.getTotalRowCount(), is(5));

    // verify the paginated list order
    List<Program> programs = pagedPrograms.getList();
    assertThat(programs.get(0).getName(), is("Behind The Iron Curtain"));
    assertThat(programs.get(1).getName(), is("Diynamic"));
    assertThat(programs.get(2).getName(), is("House Party"));
    assertThat(programs.get(3).getName(), is("Systematic Session"));
    assertThat(programs.get(4).getName(), is("Transitions"));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findById(1L);
    assertTrue(maybeProgram.isPresent());
    assertEquals("Transitions", maybeProgram.get().getName());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findById(999L);
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void findByName_successGivenNameInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findByName("Transitions");
    assertTrue(maybeProgram.isPresent());
    assertEquals("Transitions", maybeProgram.get().getName());
  }

  @Test public void findByName_failureGivenNameNotInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findByName("Full Metal Racket");
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void findBySlug_successGivenSlugInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findBySlug("transitions");
    assertTrue(maybeProgram.isPresent());
    assertEquals("Transitions", maybeProgram.get().getName());
  }

  @Test public void findBySlug_failureGivenSlugNotInDb() throws Exception {
    Optional<Program> maybeProgram = programRepository.findBySlug("full-metal-racket");
    assertFalse(maybeProgram.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Channel triplej = channelRepository.findById(2L).orElseThrow(Exception::new);
    Artist artist = artistRepository.findById(1L).orElseThrow(Exception::new);
    Program program = new Program(
        null, "Mix Up", "mix-up", null,
        "We bring you \"Excluuuussssivvvvveeee\" mixes from some of the best DJs locally and from around the globe. Make sure you are wearing the right shoes, or you might not get in.",
        triplej, new ArrayList<Artist>() {{ add(artist); }}, Status.active, null, null
    );

    // ACT
    programRepository.insert(program);

    // ASSERT
    Optional<Program> maybeProgram = programRepository.findByName("Mix Up");
    assertTrue(maybeProgram.isPresent());
    assertThat(maybeProgram.get().getName(), is("Mix Up"));
    // verify that default fields are populated
    assertNotNull(maybeProgram.get().getId());
    assertNotNull(maybeProgram.get().getCreated());
    assertNotNull(maybeProgram.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an program to update
    Program program = programRepository.findById(2L).orElseThrow(Exception::new);
    // update data
    program.setName("Friday Night Shuffle");

    // ACT
    programRepository.update(program);

    // ASSERT
    Optional<Program> maybeProgram = programRepository.findById(2L);
    assertTrue(maybeProgram.isPresent());
    // verify that the program saved correctly
    assertThat(maybeProgram.get().getName(), is("Friday Night Shuffle"));
    // verify that the original image field wasn't changed
    assertThat(maybeProgram.get().getImage(), is("behind-the-iron-curtain.jpg"));
  }
}
