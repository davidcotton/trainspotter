package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;
import static models.Role.SUPER_ADMIN;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import javax.inject.Inject;
import models.create.CreateGenre;
import models.update.UpdateGenre;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.GenreService;
import services.TracklistService;
import views.html.genre.add;
import views.html.genre.edit;
import views.html.genre.index;
import views.html.genre.view;
import views.html.notFound;

public class GenreController extends Controller {

  private final GenreService genreService;
  private final FormFactory formFactory;
  private final TracklistService tracklistService;

  @Inject
  public GenreController(
      GenreService genreService,
      FormFactory formFactory,
      TracklistService tracklistService
  ) {
    this.genreService = requireNonNull(genreService);
    this.formFactory = requireNonNull(formFactory);
    this.tracklistService = requireNonNull(tracklistService);
  }

  /**
   * View all genres.
   *
   * @return A page with all genres.
   */
  public Result index() {
    return ok(index.render(genreService.fetchAll()));
  }

  /**
   * View a genre and tracklist of that genre.
   *
   * @param slug The genre's slug.
   * @param page The paginator page number.
   * @return A genre page if found.
   */
  public Result view(String slug, int page) throws Exception {
    return genreService
        .findBySlug(slug)
        .map(
            genre -> ok(
                view.render(genre, tracklistService.findAllPagedByGenre(genre, page))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateGenre.class)));
  }

  @Restrict({@Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    return genreService
        .insert(formFactory.form(CreateGenre.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form)),
            genre -> Results.redirect(routes.GenreController.view(genre.getSlug(), 1))
        );
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(String slug) {
    return genreService
        .findBySlug(slug)
        .map(genre -> ok(edit.render(
            genre,
            formFactory.form(UpdateGenre.class).fill(new UpdateGenre(genre))
        )))
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(String slug) {
    return genreService
        .findBySlug(slug)
        .map(savedGenre -> genreService
            .update(savedGenre, formFactory.form(UpdateGenre.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedGenre, form)),
                newGenre -> Results.redirect(routes.GenreController.view(newGenre.getSlug(), 1))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR)})
  public Result delete(String slug) {
    return genreService
        .findBySlug(slug)
        .map(genre -> {
          genreService.delete(genre);
          return Results.redirect(routes.GenreController.index());
        })
        .orElse(notFound(notFound.render()));
  }
}
