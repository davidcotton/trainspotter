package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;

import org.abstractj.kalium.keys.AuthenticationKey;

import javax.inject.Provider;

import play.Configuration;
import play.Environment;

import static java.util.Objects.requireNonNull;

public class ApplicationModule extends AbstractModule {

  private final Environment environment;
  private final Configuration configuration;

  public ApplicationModule(Environment environment, Configuration configuration) {
    this.environment = requireNonNull(environment);
    this.configuration = requireNonNull(configuration);
  }

  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(AuthenticationKey.class).toProvider(
        (Provider<AuthenticationKey>) () -> new AuthenticationKey(
            configuration.getString("play.crypto.secret").getBytes()
        )
    );
  }
}
