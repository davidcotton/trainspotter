package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import play.inject.ApplicationLifecycle;
import play.libs.Json;

public class JsonModule extends AbstractModule {

  /**
   * Configures a {@link Binder} via the exposed methods.
   */
  @Override
  protected void configure() {
    //bind(ObjectMapper.class).toProvider(ObjectMapperProvider.class).asEagerSingleton();
    bind(ZonedDateTimeSerializer.class).asEagerSingleton();
    bind(LocalDateSerializer.class).asEagerSingleton();
  }

  @Singleton
  public static class ObjectMapperProvider implements Provider<ObjectMapper> {

    private final ApplicationLifecycle lifecycle;

    @Inject
    public ObjectMapperProvider(ApplicationLifecycle lifecycle) {
      this.lifecycle = lifecycle;
    }

    /**
     * Create a new Jackson {@link ObjectMapper} that will serialize JSON using snake_case keys.
     */
    @Override
    public ObjectMapper get() {
      ObjectMapper mapper = Json.newDefaultMapper()
          .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

      // update Json helper to use our new ObjectMapper
      Json.setObjectMapper(mapper);
      // cleanup the ObjectMapper on destruct
      lifecycle.addStopHook(() -> {
        Json.setObjectMapper(null);
        return CompletableFuture.completedFuture(null);
      });

      return mapper;
    }
  }

  /**
   * Serialize java.time.ZonedDateTime objects in the JSON response.
   */
  @Singleton
  public static class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    @Inject
    public ZonedDateTimeSerializer() {
      super(ZonedDateTime.class);
      register(Json.mapper());
    }

    private ObjectMapper register(ObjectMapper mapper) {
      SimpleModule module = new SimpleModule();
      module.addSerializer(ZonedDateTime.class, this);
      mapper.registerModule(module);

      return mapper;
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      gen.writeString(value.format(DateTimeFormatter.ISO_INSTANT));
    }
  }

  /**
   * Serialize java.time.LocalDate objects in the JSON response.
   */
  @Singleton
  public static class LocalDateSerializer extends StdSerializer<LocalDate> {

    @Inject
    public LocalDateSerializer() {
      super(LocalDate.class);
      register(Json.mapper());
    }

    private ObjectMapper register(ObjectMapper mapper) {
      SimpleModule module = new SimpleModule();
      module.addSerializer(LocalDate.class, this);
      mapper.registerModule(module);

      return mapper;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
  }
}
