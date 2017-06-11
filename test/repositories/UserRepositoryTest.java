package repositories;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static play.inject.Bindings.bind;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.avaje.ebean.Ebean;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import models.CreateUser;
import models.User;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.Mode;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.WithApplication;

public class UserRepositoryTest extends WithApplication {

  private UserRepository userRepository;
  private Database database;

  public UserRepositoryTest() {
    this.userRepository = new UserRepository();
  }

  @Override
  protected Application provideApplication() {
    database = Databases.createFrom(
        "com.mysql.jdbc.Driver",
        "jdbc:mysql://localhost:3306/trainspotter_test?useSSL=false",
        ImmutableMap.of("username", "root")
    );
//    database = Databases.inMemory(
//        "default",
//        ImmutableMap.of("MODE", "MYSQL"),
//        ImmutableMap.of("logStatements", true)
//    );

    GuiceApplicationBuilder builder = new GuiceApplicationLoader()
        .builder(new ApplicationLoader.Context(Environment.simple()));

    Guice.createInjector(builder.applicationModule()).injectMembers(this);

    return builder
        .overrides(bind(Database.class).toInstance(database))
        .in(Mode.TEST)
        .build();
  }

  @Before public void setUp() {
    Evolutions.applyEvolutions(database);
    Ebean.execute(Ebean.createCallableSql(
        "INSERT INTO `user` (`email`, `hash`, `salt`, `display_name`, `status`, `created`, `updated`)\n" +
        "VALUES\n" +
        "  ('john.digweed@bedrock-records.com', '$2a$16$T1PaqXFutgw9qUmlK875Ge4wFRnn9TBMyJHfxyBpDXItcrNDL/OYa', '$2a$16$T1PaqXFutgw9qUmlK875Ge', 'John Digweed', 'active', NOW(), NOW()),\n" +
        "  ('sasha@last-night-on-earth.com', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.xKIJUcj9lInDBANSKNmiS5WCKW7uvai', '$2a$16$JzMtqiUzAsUkWn1AYe.1C.', 'Sasha', 'deleted', NOW(), NOW()),\n" +
        "  ('adam.beyer@drumcode.com', '$2a$16$aY..e8GAU2YGfdvLGqtaheWo5I7vwq9SPc7bqX8hgbgdSQEVUYGSq', '$2a$16$aY..e8GAU2YGfdvLGqtahe', 'Adam Beyer', 'active', NOW(), NOW())\n" +
        ";"
    ));
  }

  @After public void tearDown() {
    Evolutions.cleanupEvolutions(database);
    database.shutdown();
  }

  @Test public void findAll() throws Exception {
    List<User> users = userRepository.findAll();

    assertThat(users, not(IsEmptyCollection.empty()));
    assertThat(users.size(), is(3));
    assertThat(users, hasItem(hasProperty("email", is("john.digweed@bedrock-records.com"))));
    assertThat(users, hasItem(hasProperty("email", is("sasha@last-night-on-earth.com"))));
    assertThat(users, hasItem(hasProperty("email", is("adam.beyer@drumcode.com"))));
  }

  @Test public void findAllCurrentUsers() throws Exception {
    List<User> users = userRepository.findAllCurrentUsers();

    assertThat(users, not(IsEmptyCollection.empty()));
    assertThat(users.size(), is(2));
    assertThat(users, hasItem(hasProperty("email", is("john.digweed@bedrock-records.com"))));
    assertThat(users, not(hasItem(hasProperty("email", is("sasha@last-night-on-earth.com")))));
    assertThat(users, hasItem(hasProperty("email", is("adam.beyer@drumcode.com"))));
  }

  @Test public void findById_successGivenIdInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findById(1L);

    assertTrue(maybeUser.isPresent());
    assertEquals("john.digweed@bedrock-records.com", maybeUser.get().getEmail());
  }

  @Test public void findById_failureGivenIdNotInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findById(999L);

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findById_failureGivenUserDeleted() throws Exception {
    Optional<User> maybeUser = userRepository.findById(2L);

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findByEmail_successGivenEmailIdInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("john.digweed@bedrock-records.com");

    assertTrue(maybeUser.isPresent());
    assertEquals("john.digweed@bedrock-records.com", maybeUser.get().getEmail());
  }

  @Test public void findByEmail_failureGivenEmailNotInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("mc.hammer@rubbish.com");

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findByEmail_failureGivenUserDeleted() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("sasha@last-night-on-earth.com");

    assertFalse(maybeUser.isPresent());
  }

  @Test public void insert_success() throws Exception {
    // ARRANGE
    String email = "solomun@diynamic.com";
    User user = new User(new CreateUser(email, "Solomun", "password1!"));

    // ACT
    userRepository.insert(user);

    // ASSERT
    Optional<User> maybeUser = userRepository.findByEmail(email);
    assertTrue(maybeUser.isPresent());
    // verify that default fields are populated
    assertNotNull(maybeUser.get().getId());
    assertThat(maybeUser.get().getStatus(), is(User.Status.unverified));
    assertNotNull(maybeUser.get().getSalt());
    assertNotNull(maybeUser.get().getHash());
    assertNotNull(maybeUser.get().getCreated());
    assertNotNull(maybeUser.get().getUpdated());
  }

  @Test public void update_success() throws Exception {
    // ARRANGE
    // insert a user
    User user = new User(new CreateUser("solomun@diynamic.com", "Solomun", "password1!"));
    userRepository.insert(user);
    // fetch the inserted user
    User savedUser = userRepository.findByEmail("solomun@diynamic.com").orElseThrow(Exception::new);
    // update data
    savedUser.setEmail("richie.hawtin@minus.com");
    savedUser.setStatus(User.Status.active);
    ZonedDateTime created = user.getCreated();

    // ACT
    userRepository.update(savedUser);

    // ASSERT
    Optional<User> maybeUser = userRepository.findByEmail("richie.hawtin@minus.com");
    assertTrue(maybeUser.isPresent());
    // verify that the user saved correctly
    assertThat(maybeUser.get().getId(), is(4L));
    assertThat(maybeUser.get().getStatus(), is(User.Status.active));
    assertNotNull(maybeUser.get().getSalt());
    assertNotNull(maybeUser.get().getHash());
    // there appears to be about 100ms gap during DB inserts
    // I think it might be due to MySQL timestamp truncating the unix time
    Comparator<ZonedDateTime> comparator = Comparator.comparing(zdt -> zdt.truncatedTo(ChronoUnit.SECONDS));
    assertThat(comparator.compare(maybeUser.get().getCreated(), created), is(0));
    assertNotNull(maybeUser.get().getUpdated());
  }
}
