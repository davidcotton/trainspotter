package controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;
import services.TokenService;

public class Security {

  /**
   * Wraps the annotated action in an <code>AuthenticatedAction</code>.
   */
  @With(AuthenticatedAction.class)
  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Authenticated {

    Class<? extends TokenAuthenticator> value() default TokenAuthenticator.class;
  }

  /**
   * Wraps another action, allowing only authenticated HTTP requests.
   * <p>
   * The user name is retrieved from the session cookie, and added to the HTTP request's
   * <code>username</code> attribute.
   */
  public static class AuthenticatedAction extends Action<Authenticated> {

    private final Injector injector;
    private final TokenService tokenService;

    @Inject
    public AuthenticatedAction(Injector injector, TokenService tokenService) {
      this.injector = injector;
      this.tokenService = tokenService;
    }

    public CompletionStage<Result> call(final Context ctx) {
      TokenAuthenticator authenticator = injector.instanceOf(configuration.value());

      try {
        long userId = authenticator.getUserId(ctx);
        ZonedDateTime expiry = authenticator.getExpiry(ctx);
        String signature = authenticator.getSignature(ctx);
        if (tokenService.isAuthorised(userId, expiry, signature)) {
          return delegate.call(ctx);
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        Result unauthorized = authenticator.onUnauthorized(ctx);
        return CompletableFuture.completedFuture(unauthorized);
      }
    }
  }

  /**
   * Handles authentication.
   */
  public static class TokenAuthenticator extends Results {

    public long getUserId(Context ctx) {
      return Long.parseLong(ctx.request().getHeader("X-Authorisation-User-Id"));
    }

    public ZonedDateTime getExpiry(Context ctx) {
      String expires = ctx.request().getHeader("X-Authorisation-Expires");
      return ZonedDateTime.parse(expires, DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getSignature(Context ctx) {
      return ctx.request().getHeader("X-Authorisation-Signature");
    }

    /**
     * Generates an alternative result if the user is not authenticated; the default a simple '401
     * Not Authorized' page.
     *
     * @param ctx the current request context
     * @return a <code>401 Not Authorized</code> result
     */
    public Result onUnauthorized(Context ctx) {
      return unauthorized();
    }
  }
}
