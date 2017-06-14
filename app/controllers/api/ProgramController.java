package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import java.util.Optional;
import javax.inject.Inject;
import models.Artist;
import models.Channel;
import models.Program;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ArtistService;
import services.ChannelService;
import services.ProgramService;

public class ProgramController extends Controller {

  private final ProgramService programService;
  private final ChannelService channelService;
  private final ArtistService artistService;

  @Inject
  public ProgramController(
      ProgramService programService,
      ChannelService channelService,
      ArtistService artistService
  ) {
    this.programService = requireNonNull(programService);
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
   * @param id The Program's ID.
   * @return Either the Program or a 404.
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
   * @return The inserted Program.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result create(long channelId) {
    Optional<Channel> maybeChannel = channelService.findById(channelId);
    return programService
        .insert(fromJson(request().body().asJson(), Program.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            program -> created(toJson(program))
        );
  }

  /**
   * Update a Program's data.
   *
   * @param id The Program's ID.
   * @return The updated Program data.
   */
  @BodyParser.Of(BodyParser.Json.class)
  public Result update(long id) {
    return programService
        .findById(id)
        .map(savedProgram -> programService
            .update(savedProgram, fromJson(request().body().asJson(), Program.class))
            .fold(
                error -> badRequest(errorsAsJson(error)),
                newProgram -> created(toJson(newProgram))
            )
        )
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  /**
   * Delete a Program from the system.
   *
   * @param id The Program's ID.
   * @return 204 No Content if successful else the error.
   */
  public Result delete(long id) {
    return TODO;
  }

  /**
   * Add an artist as a host of a program.
   *
   * Assumes both the Program & Artist already exists in the system.
   *
   * @param programId The ID of the program.
   * @param artistId  The ID of the artist.
   * @return The updated Program if successful else the errors.
   */
  public Result addHost(long programId, long artistId) {
    Optional<Program> maybeProgram = programService.findById(programId);
    Optional<Artist> maybeArtist = artistService.findById(artistId);
    if (!maybeProgram.isPresent() || !maybeArtist.isPresent()) {
      return notFound(errorsAsJson(MESSAGE_NOT_FOUND));
    }

    Program program = programService.addHost(maybeProgram.get(), maybeArtist.get());

    return ok(toJson(program));
  }
}
