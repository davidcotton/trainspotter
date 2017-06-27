package services;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import io.atlassian.fugue.Either;
import models.Artist;
import models.Program;
import models.Program.Status;
import models.create.CreateProgram;
import models.update.UpdateProgram;
import play.data.Form;
import repositories.ProgramRepository;

public class ProgramService {

  private final ProgramRepository programRepository;

  @Inject
  public ProgramService(ProgramRepository programRepository) {
    this.programRepository = requireNonNull(programRepository);
  }

  /**
   * Fetch all Programs.
   *
   * @return A collection of all Programs.
   */
  public List<Program> fetchAll() {
    return programRepository.findAll();
  }

  /**
   * Fetch all Programs that belong to a Channel.
   *
   * @param channelId The Channel ID to search by.
   * @return A collection of Programs
   */
  public List<Program> fetchByChannel(long channelId) {
    return programRepository.findByChannel(channelId);
  }

  /**
   * Find an Program by its ID.
   *
   * @param id  The ID to search for.
   * @return    An optional Program if found.
   */
  public Optional<Program> findById(long id) {
    return programRepository.findById(id);
  }

  /**
   * Find an Program by its name.
   *
   * @param name  The name to search for.
   * @return      An optional Program if found.
   */
  public Optional<Program> findByName(String name) {
    return programRepository.findByName(name);
  }

  public Optional<Program> findBySlug(String slug) {
    return programRepository.findBySlug(slug);
  }

  /**
   * Insert a new Program.
   *
   * @param programForm The submitted Program data form.
   * @return Either the inserted Program or the form with errors.
   */
  public Either<Form<CreateProgram>, Program> insert(Form<CreateProgram> programForm) {
    if (programForm.hasErrors()) {
      return Either.left(programForm);
    }

    Program program = new Program(programForm.get());
    // save to DB
    programRepository.insert(program);

    // return saved program
    return Either.right(program);
  }

  /**
   * Update an Program.
   *
   * @param savedProgram The existing Program.
   * @param programForm  The new Program data form.
   * @return Either the updated Program or the form with errors.
   */
  public Either<Form<UpdateProgram>, Program> update(Program savedProgram, Form<UpdateProgram> programForm) {
    if (programForm.hasErrors()) {
      return Either.left(programForm);
    }

    UpdateProgram updateProgram = programForm.get();
    Program newProgram = new Program(updateProgram, savedProgram);

    // save to DB
    programRepository.update(newProgram);

    // return saved program
    return Either.right(newProgram);
  }

  /**
   * Delete a Program.
   *
   * @param program The Program to delete.
   */
  public void delete(Program program) {
    program.setStatus(Status.deleted);
    programRepository.update(program);
  }

  /**
   * Add an Artist as a host of a Program.
   *
   * @param program The Program.
   * @param artist  The host (Artist).
   * @return The updated Program.
   */
  public Program addHost(Program program, Artist artist) {
    program.addHost(artist);
    programRepository.update(program);

    return program;
  }
}
