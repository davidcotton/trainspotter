package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import services.UserService;

public class CombinedHandlerCache implements HandlerCache {

  private final SessionDeadboltHandler defaultHandler;
  private final Map<String, DeadboltHandler> handlers = new HashMap<>();

  @Inject
  public CombinedHandlerCache(final ExecutionContextProvider ecProvider, UserService userService) {
    defaultHandler = new SessionDeadboltHandler(ecProvider, userService);
    handlers.put(HandlerKeys.DEFAULT.key, defaultHandler);
    handlers.put(HandlerKeys.TOKEN.key, new TokenDeadboltHandler(ecProvider, userService));
    handlers.put(HandlerKeys.NO_USER.key, new NoUserDeadboltHandler(ecProvider));
  }

  /**
   * Applies this function to the given argument.
   *
   * @param key the function argument
   * @return the function result
   */
  @Override
  public DeadboltHandler apply(String key) {
    return handlers.get(key);
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
