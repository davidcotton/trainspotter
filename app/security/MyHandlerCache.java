package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;
import models.User;
import play.Logger;
import services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

public class MyHandlerCache implements HandlerCache {

  private final MyDeadboltHandler defaultHandler;
  private final Map<String, DeadboltHandler> handlers = new HashMap<>();

  @Inject
//  public MyHandlerCache(final ExecutionContextProvider ecProvider) {
  public MyHandlerCache(final ExecutionContextProvider ecProvider, UserService userService) {
    defaultHandler = new MyDeadboltHandler(ecProvider, userService);
//    defaultHandler = new MyDeadboltHandler(ecProvider);

    Optional<User> maybeUser = userService.findActiveById(1);
    if (maybeUser.isPresent()) {
      Logger.info(maybeUser.get().getUsername());
    }

    handlers.put(HandlerKeys.DEFAULT.key, defaultHandler);
    handlers.put(HandlerKeys.ALT.key, new MyAlternativeDeadboltHandler(ecProvider));
    handlers.put(HandlerKeys.BUGGY.key, new BuggyDeadboltHandler(ecProvider));
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
