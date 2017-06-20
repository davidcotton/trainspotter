package repositories;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import models.Artist;
import models.Media;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

public class MediaRepositoryTest extends AbstractIntegrationTest {

  private MediaRepository mediaRepository;
  private ArtistRepository artistRepository;

  public MediaRepositoryTest() {
    mediaRepository = new MediaRepository();
    artistRepository = new ArtistRepository();
  }

  @Test public void findAll() throws Exception {
    List<Media> medias = mediaRepository.findAll();
    assertThat(medias, not(IsEmptyCollection.empty()));
    assertThat(medias.size(), is(3));
    assertThat(medias, hasItem(hasProperty("url", is("https://youtu.be/tI7ywh2sI04"))));
    assertThat(medias, hasItem(hasProperty("url", is("https://www.mixcloud.com/globaldjmix/john-digweed-jesper-dahlback-transitions-664-2017-05-19/"))));
    assertThat(medias, hasItem(hasProperty("url", is("https://soundcloud.com/john-digweed"))));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<Media> maybeMedia = mediaRepository.findById(1L);
    assertTrue(maybeMedia.isPresent());
    assertEquals("https://youtu.be/tI7ywh2sI04", maybeMedia.get().getUrl());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<Media> maybeMedia = mediaRepository.findById(999L);
    assertFalse(maybeMedia.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    Artist artist = artistRepository.findById(1L).orElseThrow(Exception::new);
    Media media = new Media(null, "https://www.youtube.com/channel/UCXUO2biGVP7FKCqPEDwmt4w", null, artist, null, null, null);

    // ACT
    mediaRepository.insert(media);

    // ASSERT
    // verify that default fields are populated
    assertNotNull(media.getId());
    assertNotNull(media.getCreated());
    assertNotNull(media.getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // fetch an media to update
    Media media = mediaRepository.findById(3L).orElseThrow(Exception::new);
    // update data
    media.setUrl("https://www.facebook.com/djjohndigweed");

    // ACT
    mediaRepository.update(media);

    // ASSERT
    Optional<Media> maybeMedia = mediaRepository.findById(3L);
    assertTrue(maybeMedia.isPresent());
    // verify that the media saved correctly
    assertThat(maybeMedia.get().getId(), is(3L));
    assertThat(maybeMedia.get().getUrl(), is("https://www.facebook.com/djjohndigweed"));
  }

  @Test public void delete() throws Exception {
    Media media = mediaRepository.findById(1L).orElseThrow(Exception::new);
    mediaRepository.delete(media);
    Optional<Media> maybeMedia = mediaRepository.findById(1L);
    assertFalse(maybeMedia.isPresent());
  }
}
