package services;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.typesafe.config.ConfigFactory;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import models.Token;
import models.User;
import models.User.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import play.Configuration;
import repositories.TokenRepository;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

  private TokenService tokenService;
  private TokenRepository mockTokenRepository;
  private static final String APPLICATION_CONF = "application.conf";

  @Before public void setUp() throws Exception {
    mockTokenRepository = mock(TokenRepository.class);
    Configuration configuration = new Configuration(ConfigFactory.load(APPLICATION_CONF));
    tokenService = new TokenService(mockTokenRepository, configuration);
  }

  @Test public void generate_successGivenNoExistingToken() {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    Optional<Token> emptyToken = Optional.empty();

    when(mockTokenRepository.findByUser(user)).thenReturn(emptyToken);

    // ACT
    Token generatedToken = tokenService.generate(user);

    // ASSERT
    assertNotNull(generatedToken);
    // verify the tokenRepository inserted the token
    verify(mockTokenRepository).insert(generatedToken);
    // verify there is a signature & expiry
    assertNotNull(generatedToken.getExpiry());
    assertNotNull(generatedToken.getSignature());
  }

  @Test public void generate_successGivenExistingToken() {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    String signature = "qbGgkOeh2GADl0oH+rdweuSe9nq60J1hAN5fvBA5FGE=";
    // old token expired an hour ago
    ZonedDateTime expiry = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(-1);
    Token oldToken = new Token(1L, user, expiry, signature);

    when(mockTokenRepository.findByUser(user)).thenReturn(Optional.of(oldToken));

    // ACT
    Token generatedToken = tokenService.generate(user);

    // ASSERT
    assertNotNull(generatedToken);
    // verify the tokenRepository updated the token
    verify(mockTokenRepository).update(generatedToken);
    // verify the ID & user are the same
    assertEquals(oldToken.getId(), generatedToken.getId());
    assertEquals(oldToken.getUser(), generatedToken.getUser());
    // verify there is a signature & expiry
    assertNotNull(generatedToken.getExpiry());
    assertNotNull(generatedToken.getSignature());
    // verify the signature & expiry are new
    assertNotEquals(expiry, generatedToken.getExpiry());
    assertNotEquals(signature, generatedToken.getSignature());
  }

  @Test public void isAuthorised_success() throws Exception {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    String signature = "qbGgkOeh2GADl0oH+rdweuSe9nq60J1hAN5fvBA5FGE=";
    ZonedDateTime expiry = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(4);
    Token token = new Token(1L, user, expiry, signature);

    when(mockTokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(token));

    // ACT
    boolean result = tokenService.isAuthorised(user.getId(), expiry, signature);

    // ASSERT
    assertTrue(result);
  }

  @Test public void isAuthorised_failureWhenNoToken() throws Exception {
    // ARRANGE
    User user = new User(
        1L, "guy.incognito@simpsons.com", "Guy Incognito", "guy-incognito", Status.active,
        0, 0, 0, "$2a$16$MdmQnCqlTMk8hXUSWM8QCONuztBiiV9DIglZfuyxwjNknA3yENMOK",
        null, null, null, null, ZonedDateTime.now(), ZonedDateTime.now()
    );
    String signature = "qbGgkOeh2GADl0oH+rdweuSe9nq60J1hAN5fvBA5FGE=";
    ZonedDateTime expiry = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(4);
    Token emptyToken = mock(Token.class);

    when(mockTokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

    // ACT
    boolean result = tokenService.isAuthorised(user.getId(), expiry, signature);

    // ASSERT
    assertFalse(result);
    // verify we didn't try to call isValid on null
    verify(emptyToken, never()).isValid(expiry, signature);
  }
}
