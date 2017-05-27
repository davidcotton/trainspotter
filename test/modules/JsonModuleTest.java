package modules;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import modules.JsonModule.LocalDateSerializer;
import modules.JsonModule.ZonedDateTimeSerializer;

import org.junit.Test;

public class JsonModuleTest {

  @Test
  public void serializesLocalDate() throws IOException {
    Writer writer = new StringWriter();
    JsonGenerator gen = new JsonFactory().createGenerator(writer);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();

    LocalDate date = LocalDate.of(1996, 1, 23);
    LocalDateSerializer serializer = new LocalDateSerializer();
    serializer.serialize(date, gen, provider);
    gen.flush();

    assertThat(writer.toString(), is(equalTo("\"1996-01-23\"")));
  }

  @Test
  public void serializesZonedDateTime() throws IOException {
    Writer writer = new StringWriter();
    JsonGenerator gen = new JsonFactory().createGenerator(writer);
    SerializerProvider provider = new ObjectMapper().getSerializerProvider();

    ZonedDateTime date = ZonedDateTime.of(LocalDateTime.of(1996, 1, 23, 12, 6, 30), ZoneId.of("UTC"));
    ZonedDateTimeSerializer serializer = new ZonedDateTimeSerializer();
    serializer.serialize(date, gen, provider);
    gen.flush();

    assertThat(writer.toString(), is(equalTo("\"1996-01-23T12:06:30Z\"")));
  }
}
