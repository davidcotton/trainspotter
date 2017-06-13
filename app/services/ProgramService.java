package services;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.toJson;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import io.atlassian.fugue.Either;
import models.Program;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import repositories.ProgramRepository;

public class ProgramService {

  private final ProgramRepository programRepository;
  private final FormFactory formFactory;

  @Inject
  public ProgramService(ProgramRepository programRepository, FormFactory formFactory) {
    this.programRepository = requireNonNull(programRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * Fetch all Programs.
   *
   * @return A collection of all Programs in the DB.
   */
  public List<Program> fetchAll() {
    return programRepository.findAll();
  }

  /**
   * Find an Program by their ID.
   *
   * @param id  The ID to search for.
   * @return    An optional Program if found.
   */
  public Optional<Program> findById(long id) {
    return programRepository.findById(id);
  }

  /**
   * Find an Program by their name.
   *
   * @param name  The name to search for.
   * @return      An optional Program if found.
   */
  public Optional<Program> findByName(String name) {
    return programRepository.findByName(name);
  }

  /**
   * Insert a new Program.
   *
   * @param program  The Program data to insert.
   * @return Either the inserted Program or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Program> insert(Program program) {
    // validate new program
    Form<Program> programForm = formFactory
        .form(Program.class, Program.InsertValidators.class)
        .bind(toJson(program));
    if (programForm.hasErrors()) {
      // return validation errors
      return Either.left(programForm.errors());
    }

    // save to DB
    programRepository.insert(program);

    // return saved program
    return Either.right(program);
  }

  /**
   * Update an Program.
   *
   * @param savedProgram The existing Program data.
   * @param newProgram   The new Program data.
   * @return Either the updated Program or validation errors.
   */
  public Either<Map<String, List<ValidationError>>, Program> update(Program savedProgram, Program newProgram) {
    // copy over read only fields
    newProgram.setId(savedProgram.getId());
    newProgram.setCreated(savedProgram.getCreated());

    // validate the changes
    Form<Program> programForm = formFactory
        .form(Program.class, Program.UpdateValidators.class)
        .bind(toJson(newProgram));
    if (programForm.hasErrors()) {
      // return validation errors
      return Either.left(programForm.errors());
    }

    // save to DB
    programRepository.update(newProgram);

    // return saved program
    return Either.right(newProgram);
  }
}
