package controllers.app;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.create.CreateProgram;
import models.update.UpdateProgram;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import security.SessionAuthenticator;
import services.ChannelService;
import services.ProgramService;
import views.html.notFound;
import views.html.program.add;
import views.html.program.edit;
import views.html.program.index;
import views.html.program.view;

public class ProgramController extends Controller {

  private final ProgramService programService;
  private final FormFactory formFactory;
  private final ChannelService channelService;

  @Inject
  public ProgramController(
      ProgramService programService,
      FormFactory formFactory,
      ChannelService channelService
  ) {
    this.programService = requireNonNull(programService);
    this.formFactory = requireNonNull(formFactory);
    this.channelService = requireNonNull(channelService);
  }

  /**
   * View all Programs.
   *
   * @param channelSlug The slug of the Channel to search for.
   * @return A page with all Programs that belong to a particular Channel.
   */
  public Result index(String channelSlug) {
    return channelService
        .findBySlug(channelSlug)
        .map(channel -> ok(index.render(programService.fetchAll(), channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a single Program.
   *
   * @param programSlug The slug of the program to search for.
   * @return A program page if found, else a 404.
   */
  public Result view(String programSlug) {
    return programService
        .findBySlug(programSlug)
        .map(program -> ok(view.render(program)))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result addForm(String channelSlug) {
    return channelService
        .findBySlug(channelSlug)
        .map(channel -> ok(add.render(formFactory.form(CreateProgram.class), channel)))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result addSubmit(String channelSlug) {
    return channelService
        .findBySlug(channelSlug)
        .map(channel -> programService
            .insert(formFactory.form(CreateProgram.class).bindFromRequest())
            .fold(
                form -> badRequest(add.render(form, channel)),
                program -> Results.redirect(routes.ProgramController.view(program.getSlug()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editForm(String programSlug) {
    return programService
        .findBySlug(programSlug)
        .map(program -> ok(edit.render(
            program,
            formFactory.form(UpdateProgram.class).fill(new UpdateProgram(program))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result editSubmit(String programSlug) {
    return programService
        .findBySlug(programSlug)
        .map(savedProgram -> programService
            .update(savedProgram, formFactory.form(UpdateProgram.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedProgram, form)),
                newProgram -> Results.redirect(routes.ProgramController.view(newProgram.getSlug()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Security.Authenticated(SessionAuthenticator.class)
  public Result delete(String programSlug) {
    return programService
        .findBySlug(programSlug)
        .map(program -> {
          programService.delete(program);
          return Results.redirect(routes.ChannelController.index(1));
        })
        .orElse(notFound(notFound.render()));
  }
}
