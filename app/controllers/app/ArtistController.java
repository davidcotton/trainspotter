package controllers.app;

import static java.util.Objects.requireNonNull;
import static models.Role.ADMIN;
import static models.Role.CONTRIBUTOR;
import static models.Role.EDITOR;
import static models.Role.SUPER_ADMIN;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import javax.inject.Inject;
import models.create.CreateArtist;
import models.update.UpdateArtist;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ArtistService;
import services.TrackService;
import services.TracklistService;
import views.html.artist.add;
import views.html.artist.edit;
import views.html.artist.index;
import views.html.artist.track;
import views.html.artist.tracklist;
import views.html.artist.view;
import views.html.notFound;

public class ArtistController extends Controller {

  private final ArtistService artistService;
  private final FormFactory formFactory;
  private final TracklistService tracklistService;
  private final TrackService trackService;

  @Inject
  public ArtistController(
      ArtistService artistService,
      FormFactory formFactory,
      TracklistService tracklistService,
      TrackService trackService
  ) {
    this.artistService = requireNonNull(artistService);
    this.formFactory = requireNonNull(formFactory);
    this.tracklistService = requireNonNull(tracklistService);
    this.trackService = requireNonNull(trackService);
  }

  /**
   * View all artists.
   *
   * @return A paginated page of artists.
   */
  public Result index(int page) {
    return ok(index.render(artistService.fetchAllPaged(page)));
  }

  /**
   * View a single artist.
   *
   * @param slug The slug of the Artist to find.
   * @return An artist page if found, else a 404.
   */
  public Result view(String slug) {
    return artistService
        .findBySlug(slug)
        .map(artist -> ok(view.render(artist)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add Artist page.
   *
   * @return A page allowing the user to add an Artist.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateArtist.class)));
  }

  /**
   * Process the submission for creating a new Artist.
   *
   * @return Redirect to the new Artist on success or the form with errors on failure.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result addSubmit() {
    return artistService
        .insert(formFactory.form(CreateArtist.class).bindFromRequest())
        .fold(
            form -> badRequest(add.render(form)),
            artist -> Results.redirect(routes.ArtistController.view(artist.getSlug()))
        );
  }

  /**
   * Display an edit artist page.
   *
   * @param slug The slug of the Artist to find.
   * @return An edit artist page if artist is found else not found page.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editForm(String slug) {
    return artistService
        .findBySlug(slug)
        .map(artist -> ok(edit.render(
            artist,
            formFactory.form(UpdateArtist.class).fill(new UpdateArtist(artist))
        )))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Process the submission for updating an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @return Redirect to updated Artist on success else the form with errors.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR), @Group(CONTRIBUTOR)})
  public Result editSubmit(String slug) {
    return artistService
        .findBySlug(slug)
        .map(savedArtist -> artistService
            .update(savedArtist, formFactory.form(UpdateArtist.class).bindFromRequest())
            .fold(
                form -> badRequest(edit.render(savedArtist, form)),
                artist -> Results.redirect(routes.ArtistController.view(artist.getSlug()))
            )
        )
        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @return Redirects back to Artists list page on success else not found.
   */
  @Restrict({@Group(SUPER_ADMIN), @Group(ADMIN), @Group(EDITOR)})
  public Result delete(String slug) {
    return artistService
        .findBySlug(slug)
        .map(artist -> {
          artistService.delete(artist);
          return Results.redirect(routes.ArtistController.index(1));
        })
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a paginated list of Tracklists by an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @param page The page number to view.
   * @return A paginated list of Tracklists if the artist is found.
   */
  public Result tracklist(String slug, int page) {
    return artistService
        .findBySlug(slug)
        .map(artist -> ok(
            tracklist.render(artist, tracklistService.findAllPagedByArtist(artist, page))
        ))
        .orElse(notFound(notFound.render()));
  }

  /**
   * View a paginated list of Tracks by an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @param page The page number to view.
   * @return A paginated list of Tracks if the artist is found.
   */
  public Result track(String slug, int page) {
    return artistService
        .findBySlug(slug)
        .map(artist -> ok(
            track.render(artist, trackService.findAllPagedByArtist(artist, page))
        ))
        .orElse(notFound(notFound.render()));
  }
}
