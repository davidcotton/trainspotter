package security;

import javax.inject.Inject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;
import services.UserService;

public class MySimpleHandlerCache implements HandlerCache {

  private final DeadboltHandler defaultHandler;

  @Inject
//  public MySimpleHandlerCache(final ExecutionContextProvider ecProvider) {
//    defaultHandler = new MyDeadboltHandler(ecProvider);
  public MySimpleHandlerCache(final ExecutionContextProvider ecProvider, UserService userService) {
    defaultHandler = new MyDeadboltHandler(ecProvider, userService);
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
