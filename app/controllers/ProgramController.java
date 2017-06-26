package controllers;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Program;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
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
   * @param channelSlug The channel ID.
   * @return A page with all programs.
   */
  public Result index(String channelSlug) {
    return channelRepository
        .findBySlug(channelSlug)
        .map(channel -> ok(index.render(programRepository.findAll(), channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a single program.
   *
   * @param programSlug The program ID.
   * @return A program page if found.
   */
  public Result view(String programSlug) {
    return programRepository
        .findBySlug(programSlug)
        .map(program -> ok(view.render(program)))
        .orElse(notFound(notFound.render()));
  }

  @play.mvc.Security.Authenticated(Secured.class)
  public Result addForm(String channelSlug) {
    return channelRepository
        .findBySlug(channelSlug)
        .map(channel -> ok(add.render(formFactory.form(Program.class), channel)))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result addSubmit(String channelSlug) {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result editForm(String programSlug) {
    return programRepository
        .findBySlug(programSlug)
        .map(program -> ok(edit.render(program, formFactory.form(Program.class).fill(program))))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(Secured.class)
  public Result editSubmit(String slug) {
    return TODO;
  }

  @Security.Authenticated(Secured.class)
  public Result delete(String slug) {
    return TODO;
  }
}
