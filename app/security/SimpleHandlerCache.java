package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;
import javax.inject.Inject;
import services.UserService;

public class SimpleHandlerCache implements HandlerCache {

  private final DeadboltHandler defaultHandler;

  @Inject
  public SimpleHandlerCache(final ExecutionContextProvider ecProvider, UserService userService) {
    defaultHandler = new SessionDeadboltHandler(ecProvider, userService);
  }

  /**
   * Applies this function to the given argument.
   *
   * @param key the function argument
   * @return the function result
   */
  @Override
  public DeadboltHandler apply(String key) {
    return defaultHandler;
  }

  /**
   * Gets a result.
   *
   * @return a result
   */
  @Override
  public DeadboltHandler get() {
    return defaultHandler;
  }
}
