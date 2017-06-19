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
import views.html.notFound;
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
    return channelRepository
        .findById(channelId)
        .map(channel -> ok(index.render(programRepository.findAll(), channel)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a single program.
   *
   * @param programId The program ID.
   * @param channelId The channel ID.
   * @return A program page if found.
   */
  public Result view(long programId, long channelId) {
    return channelRepository
        .findById(channelId)
        .flatMap(channel -> programRepository
            .findById(programId)
            .map(program -> ok(view.render(program, channel)))
        )
        .orElse(notFound(notFound.render()));
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
