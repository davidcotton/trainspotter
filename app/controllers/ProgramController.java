package controllers;

import java.util.Optional;
import javax.inject.Inject;

import models.Channel;
import models.Program;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.ChannelRepository;
import repositories.ProgramRepository;
import views.html.program.add;
import views.html.program.edit;
import views.html.program.index;
import views.html.program.view;

import static java.util.Objects.requireNonNull;

public class ProgramController extends Controller {

  private final ProgramRepository programRepository;
  private final FormFactory formFactory;
  private final ChannelRepository channelRepository;

  @Inject
  public ProgramController(ProgramRepository programRepository, FormFactory formFactory, ChannelRepository channelRepository) {
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
    Channel channel = channelRepository.findById(channelId).orElseThrow(RuntimeException::new);
    return ok(index.render(programRepository.findAll(), channel));
  }

  /**
   * View a single program.
   *
   * @param id        The program's ID.
   * @param channelId The channel ID.
   * @return A program page if found.
   */
  public Result view(long id, long channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(RuntimeException::new);
    Optional<Program> maybeProgram = programRepository.findById(id);
    return ok(view.render(maybeProgram.get(), channel));
  }

  public Result add(long channelId) {
    return TODO;
  }

  public Result edit(long id, long channelId) {
    return TODO;
  }

  public Result delete(long id, long channelId) {
    return TODO;
  }
}
