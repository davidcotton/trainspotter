package modules;

import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import javax.xml.bind.Binder;
import security.MyHandlerCache;
import security.MySimpleHandlerCache;

public class SecurityModule extends AbstractModule {

  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(HandlerCache.class).to(MySimpleHandlerCache.class);
//    bind(HandlerCache.class).to(MyHandlerCache.class);
  }
}
