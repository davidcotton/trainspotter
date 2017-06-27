package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import java.util.Optional;
import javax.inject.Inject;
import models.Channel;
import models.Program;
import models.create.CreateProgram;
import models.update.UpdateProgram;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArtistService;
import services.ChannelService;
import services.ProgramService;

public class ProgramController extends Controller {

  private final ProgramService programService;
  private final FormFactory formFactory;
  private final ChannelService channelService;
  private final ArtistService artistService;

  @Inject
  public ProgramController(
      ProgramService programService,
      FormFactory formFactory,
      ChannelService channelService,
      ArtistService artistService
  ) {
    this.programService = requireNonNull(programService);
    this.formFactory = requireNonNull(formFactory);
    this.channelService = requireNonNull(channelService);
    this.artistService = requireNonNull(artistService);
  }

  /**
   * Fetch all Programs.
   *
   * @return A collection of Programs.
   */
  public Result fetchAll() {
    return ok(toJson(programService.fetchAll()));
  }

  /**
   * Fetch all Programs that belong to Channel.
   *
   * @param channelId The Channel ID to search by.
   * @return A collection of programs.
   */
  public Result fetchByChannel(long channelId) {
    return ok(toJson(programService.fetchByChannel(channelId)));
  }

  /**
   * Fetch a single Program by its ID.
   *
   * @param id The ID of the Program to search for.
   * @return The Program if found else a 404.
   */
  public Result fetch(long id) {
    return programService.findById(id)
        .map(program -> ok(toJson(program)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Insert a new Program, belonging to a Channel.
   *
   * @param channelId The Channel the Program will belong to.
   * @return The new Program on success, else any errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create(long channelId) {
    return channelService
        .findById(channelId)
        .map(channel -> programService
            .insert(formFactory.form(CreateProgram.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                program -> created(toJson(program))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Update a Program's data.
   *
   * @param id The ID of the Program to update.
   * @return The updated Program on success, else any errors.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return programService
        .findById(id)
        .map(savedProgram -> programService
            .update(savedProgram, formFactory.form(UpdateProgram.class).bindFromRequest())
            .fold(
                form -> badRequest(errorsAsJson(form.errors())),
                newProgram -> created(toJson(newProgram))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Delete a Program.
   *
   * @param id The ID of the Program to delete.
   * @return 204 No Content if successful, else any errors.
   */
  public Result delete(long id) {
    return programService
        .findById(id)
        .map(program -> {
          programService.delete(program);
          return (Result) noContent();
        })
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Add an artist as a host of a program.
   *
   * Assumes both the Program & Artist already exists in the system.
   *
   * @param programId The ID of the program.
   * @param artistId  The ID of the artist.
   * @return The updated Program if successful, else any errors.
   */
  public Result addHost(long programId, long artistId) {
    return artistService
        .findById(artistId)
        .flatMap(artist -> programService
            .findById(programId)
            .map(program -> ok(toJson(programService.addHost(program, artist))))
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }
}
