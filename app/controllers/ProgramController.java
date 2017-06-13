package controllers;

import java.util.Optional;
import javax.inject.Inject;
import models.Program;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ProgramRepository;
import views.html.program.add;
import views.html.program.edit;
import views.html.program.index;
import views.html.program.view;

import static java.util.Objects.requireNonNull;

public class ProgramController extends Controller {

  private final ProgramRepository programRepository;
  private final FormFactory formFactory;

  @Inject
  public ProgramController(ProgramRepository programRepository, FormFactory formFactory) {
    this.programRepository = requireNonNull(programRepository);
    this.formFactory = requireNonNull(formFactory);
  }

  /**
   * View all programs.
   *
   * @return A page with all programs.
   */
  public Result index() {
    return ok(index.render(programRepository.findAll()));
  }

  /**
   * View a single program.
   *
   * @param id The program's ID.
   * @return A program page if found.
   */
  public Result view(long id) {
    Optional<Program> maybeProgram = programRepository.findById(id);
    return ok(view.render(maybeProgram.get()));
  }

  public Result add() {
    return TODO;
  }

  public Result edit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
