package controllers.api;

import static java.util.Objects.requireNonNull;
import static play.libs.Json.fromJson;
import static play.libs.Json.toJson;
import static utilities.JsonHelper.MESSAGE_NOT_FOUND;
import static utilities.JsonHelper.errorsAsJson;

import javax.inject.Inject;
import models.Program;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProgramService;

public class ProgramController extends Controller {

  private final ProgramService programService;

  @Inject
  public ProgramController(ProgramService programService) {
    this.programService = requireNonNull(programService);
  }

  public Result fetchAll() {
    return ok(toJson(programService.fetchAll()));
  }

  public Result fetch(long id) {
    return programService.findById(id)
        .map(program -> ok(toJson(program)))
        .orElse(notFound(errorsAsJson(MESSAGE_NOT_FOUND)));
  }

  @BodyParser.Of(BodyParser.Json.class)
  public Result create() {
    return programService
        .insert(fromJson(request().body().asJson(), Program.class))
        .fold(
            error -> badRequest(errorsAsJson(error)),
            program -> created(toJson(program))
        );
  }

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

  public Result delete(long id) {
    return TODO;
  }
}
