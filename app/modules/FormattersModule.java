package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.OutOfScopeException;
import com.google.inject.ProvisionException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import play.data.format.Formatters;
import play.i18n.MessagesApi;

public class FormattersModule extends AbstractModule {

  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    bind(Formatters.class).toProvider(FormattersProvider.class);
  }

  /**
   * Providers allow on demand dependency injection.
   * Formatters assist with mapping complex type in Play forms.
   */
  @Singleton
  public static class FormattersProvider implements Provider<Formatters> {

    private final MessagesApi messagesApi;

    @Inject
    public FormattersProvider(MessagesApi messagesApi) {
      this.messagesApi = messagesApi;
    }

    /**
     * Provides an instance of {@code T}. Must never return {@code null}.
     *
     * @throws OutOfScopeException When an attempt is made to access a scoped object while the
     *                             scope in question is not currently active.
     * @throws ProvisionException  If an instance cannot be provided. Such exceptions include
     *                             messages and throwables to describe why provision failed.
     */
    @Override
    public Formatters get() {
      Formatters formatters = new Formatters(messagesApi);

      // register a LocalDate formatter
      formatters.register(LocalDate.class, new Formatters.SimpleFormatter<LocalDate>() {

        @Override
        public LocalDate parse(String text, Locale locale) throws ParseException {
          return LocalDate.parse(text);
        }

        @Override
        public String print(LocalDate localDate, Locale locale) {
          return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
      });

      // register a ZonedDateTime formatter
      formatters.register(ZonedDateTime.class, new Formatters.SimpleFormatter<ZonedDateTime>() {

        @Override
        public ZonedDateTime parse(String text, Locale locale) throws ParseException {
          return ZonedDateTime.parse(text);
        }

        @Override
        public String print(ZonedDateTime zonedDateTime, Locale locale) {
          return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
        }
      });

      return formatters;
    }
  }
}
