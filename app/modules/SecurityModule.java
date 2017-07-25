package modules;

import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import security.CombinedHandlerCache;

public class SecurityModule extends AbstractModule {

  @Override
  protected void configure() {
//    bind(HandlerCache.class).to(SimpleHandlerCache.class);
    bind(HandlerCache.class).to(CombinedHandlerCache.class);
  }
}
