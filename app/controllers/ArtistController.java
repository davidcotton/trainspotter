package controllers;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import models.Artist;
import models.CreateArtist;
import models.UpdateArtist;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.mvc.Security;
import repositories.ArtistRepository;
import repositories.TrackRepository;
import repositories.TracklistRepository;
import views.html.artist.add;
import views.html.artist.edit;
import views.html.artist.index;
import views.html.artist.track;
import views.html.artist.tracklist;
import views.html.artist.view;
import views.html.notFound;

public class ArtistController extends Controller {

  private final ArtistRepository artistRepository;
  private final FormFactory formFactory;
  private final TracklistRepository tracklistRepository;
  private final TrackRepository trackRepository;

  @Inject
  public ArtistController(
      ArtistRepository artistRepository,
      FormFactory formFactory,
      TracklistRepository tracklistRepository,
      TrackRepository trackRepository
  ) {
    this.artistRepository = requireNonNull(artistRepository);
    this.formFactory = requireNonNull(formFactory);
    this.tracklistRepository = requireNonNull(tracklistRepository);
    this.trackRepository = requireNonNull(trackRepository);
  }

  /**
   * View all artists.
   *
   * @return A paginated page of artists.
   */
  public Result index(int page) {
    return ok(index.render(artistRepository.findAllPaged(page)));
  }

  /**
   * View a single artist.
   *
   * @param slug The slug of the Artist to find.
   * @return An artist page if found else not found page.
   */
  public Result view(String slug) {
    return artistRepository
        .findBySlug(slug)
        .map(artist -> ok(view.render(artist)))
        .orElse(notFound(notFound.render()));
  }

  /**
   * Display an add artist page.
   *
   * @return A page allowing the user to add an artist.
   */
  @Security.Authenticated(Secured.class)
  public Result addForm() {
    return ok(add.render(formFactory.form(CreateArtist.class)));
  }

  /**
   * Process the submission for creating a new Artist.
   *
   * @return Redirect to the new Artist on success or the form with errors.
   */
  @Security.Authenticated(Secured.class)
  public Result addSubmit() {
    Form<CreateArtist> artistForm = formFactory.form(CreateArtist.class).bindFromRequest();
    if (artistForm.hasErrors()) {
      return badRequest(add.render(artistForm));
    }

    Artist artist = new Artist(artistForm.get());
    artistRepository.insert(artist);

    return Results.redirect(routes.ArtistController.view(artist.getSlug()));
  }

  /**
   * Display an edit artist page.
   *
   * @param slug The slug of the Artist to find.
   * @return An edit artist page if artist is found else not found page.
   */
  @Security.Authenticated(Secured.class)
  public Result editForm(String slug) {
    return artistRepository
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
   * @return Redirect to updated Artist on success or the form with errors.
   */
  @Security.Authenticated(Secured.class)
  public Result editSubmit(String slug) {
    Optional<Artist> maybeArtist = artistRepository.findBySlug(slug);
    if (!maybeArtist.isPresent()) {
      return notFound(notFound.render());
    }
    Artist artist = maybeArtist.get();

    Form<UpdateArtist> artistForm = formFactory.form(UpdateArtist.class).bindFromRequest();
    if (artistForm.hasErrors()) {
      return badRequest(edit.render(artist, artistForm));
    }

    UpdateArtist updateArtist = artistForm.get();
    // copy over new fields
    artist.setName(updateArtist.getName());
    artist.setImage(updateArtist.getImage());
    artist.setDescription(updateArtist.getDescription());

    artistRepository.update(artist);

    return Results.redirect(routes.ArtistController.view(artist.getSlug()));

//    return artistRepository
//        .findBySlug(slug)
//        .map(artist -> )
//        .orElse(notFound(notFound.render()));
  }

  /**
   * Delete an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @return
   */
  @Security.Authenticated(Secured.class)
  public Result delete(String slug) {
    return TODO;
  }

  /**
   * View a paginated list of Tracklists by an Artist.
   *
   * @param slug The slug of the Artist to find.
   * @param page The page number to view.
   * @return A paginated list of Tracklists if the artist is found.
   */
  public Result tracklist(String slug, int page) {
    return artistRepository
        .findBySlug(slug)
        .map(artist -> ok(
            tracklist.render(artist, tracklistRepository.findAllPagedByArtist(artist, page))
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
    return artistRepository
        .findBySlug(slug)
        .map(artist -> ok(
            track.render(artist, trackRepository.findAllPagedByArtist(artist, page))
        ))
        .orElse(notFound(notFound.render()));
  }
}
