package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Program;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ChannelRepository;
import repositories.ProgramRepository;
import views.html.notFound;
import views.html.program.add;
import views.html.program.edit;
import views.html.program.index;
import views.html.program.view;

public class ProgramController extends Controller {

  private final ProgramRepository programRepository;
  private final FormFactory formFactory;
  private final ChannelRepository channelRepository;

  @Inject
  public ProgramController(
      ProgramRepository programRepository,
      FormFactory formFactory,
      ChannelRepository channelRepository
  ) {
    this.programRepository = requireNonNull(programRepository);
    this.formFactory = requireNonNull(formFactory);
    this.channelRepository = requireNonNull(channelRepository);
  }

  /**
   * View all programs.
   *
   * @param channelId The channel ID.
   * @return A page with all programs.
   */
  public Result index(long channelId) {
    return channelRepository
        .findById(channelId)
        .map(channel -> ok(index.render(programRepository.findAll(), channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a single program.
   *
   * @param id The program ID.
   * @return A program page if found.
   */
  public Result view(long id) {
    return programRepository
        .findById(id)
        .map(program -> ok(view.render(program)))
        .orElse(notFound(notFound.render()));
  }

  public Result addForm(long channelId) {
    return channelRepository
        .findById(channelId)
        .map(channel -> ok(add.render(formFactory.form(Program.class), channel)))
        .orElse(notFound(notFound.render()));
  }

  public Result addSubmit(long channelId) {
    return TODO;
  }

  public Result editForm(long id) {
    return programRepository
        .findById(id)
        .map(program -> ok(edit.render(id, formFactory.form(Program.class).fill(program))))
        .orElse(notFound(notFound.render()));
  }

  public Result editSubmit(long id) {
    return TODO;
  }

  public Result delete(long id) {
    return TODO;
  }
}
