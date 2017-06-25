package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;

public class Secured extends play.mvc.Security.Authenticator {

//  public static boolean isLoggedIn(Context ctx) {
//    return (getUser(ctx) != null);
//  }

  /**
   * Retrieves the username from the HTTP context; the default is to read from the session cookie.
   *
   * @param ctx the current request context
   * @return null if the user is not authenticated.
   */
  @Override
  public String getUsername(Context ctx) {
    return ctx.session().get("email");
  }

  public static String getUser(Context ctx) {
    return ctx.session().get("email");
  }

  /**
   * Generates an alternative result if the user is not authenticated; the default a simple '401 Not
   * Authorized' page.
   *
   * @param ctx the current request context
   * @return a <code>401 Not Authorized</code> result
   */
  @Override
  public Result onUnauthorized(Context ctx) {
    return redirect(routes.UserController.loginForm());
  }
}
