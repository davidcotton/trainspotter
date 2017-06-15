package repositories;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import models.CreateUser;
import models.User;

public class UserRepositoryTest extends AbstractIntegrationTest {

  private final UserRepository userRepository;

  public UserRepositoryTest() {
    userRepository = new UserRepository();
  }

  @Test public void findAll() throws Exception {
    List<User> users = userRepository.findAll();

    assertThat(users, not(IsEmptyCollection.empty()));
    assertThat(users.size(), is(3));
    assertThat(users, hasItem(hasProperty("email", is("brian.mcgee@simpsons.com"))));
    assertThat(users, hasItem(hasProperty("email", is("rembrandt.q.einstein@simpsons.com"))));
    assertThat(users, hasItem(hasProperty("email", is("rory.b.bellows@simpsons.com"))));
  }

  @Test public void findAllActiveUsers() throws Exception {
    List<User> users = userRepository.findAllActiveUsers();

    assertThat(users, not(IsEmptyCollection.empty()));
    assertThat(users.size(), is(2));
    assertThat(users, hasItem(hasProperty("email", is("brian.mcgee@simpsons.com"))));
    assertThat(users, not(hasItem(hasProperty("email", is("rembrandt.q.einstein@simpsons.com")))));
    assertThat(users, hasItem(hasProperty("email", is("rory.b.bellows@simpsons.com"))));
  }

  @Test public void findActiveById_successGivenIdInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findActiveById(1L);

    assertTrue(maybeUser.isPresent());
    assertEquals("brian.mcgee@simpsons.com", maybeUser.get().getEmail());
  }

  @Test public void findActiveById_failureGivenIdNotInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findActiveById(999L);

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findActiveById_failureGivenUserDeleted() throws Exception {
    Optional<User> maybeUser = userRepository.findActiveById(2L);

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findByEmail_successGivenEmailIdInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("brian.mcgee@simpsons.com");

    assertTrue(maybeUser.isPresent());
    assertEquals("brian.mcgee@simpsons.com", maybeUser.get().getEmail());
  }

  @Test public void findByEmail_failureGivenEmailNotInDb() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("mc.hammer@rubbish.com");

    assertFalse(maybeUser.isPresent());
  }

  @Test public void findByEmail_failureGivenUserDeleted() throws Exception {
    Optional<User> maybeUser = userRepository.findByEmail("rembrandt.q.einstein@simpsons.com");

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
