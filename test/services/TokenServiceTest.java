package services;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.abstractj.kalium.keys.AuthenticationKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Optional;
import models.Token;
import models.User;
import repositories.TokenRepository;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

  @InjectMocks private TokenService tokenService;
  @Mock private TokenRepository mockTokenRepository;
  @Mock private AuthenticationKey mockAuthenticationKey;

  @Test public void create_successGivenNoExistingToken() {
    // ARRANGE
    User mockUser = mock(User.class);
    Optional<Token> emptyToken = Optional.empty();

    when(mockTokenRepository.findByUser(mockUser)).thenReturn(emptyToken);

    // ACT
    Token token = tokenService.create(mockUser);

    // ASSERT
    assertNotNull(token);
    assertThat(token, instanceOf(Token.class));
    verify(mockTokenRepository).insert(token);
  }

  @Test public void create_successGivenExistingToken() {
    // ARRANGE
    User mockUser = mock(User.class);
    Token oldToken = mock(Token.class);

    when(mockTokenRepository.findByUser(mockUser)).thenReturn(Optional.of(oldToken));

    // ACT
    Token token = tokenService.create(mockUser);

    // ASSERT
    assertNotNull(token);
    assertThat(token, instanceOf(Token.class));
    verify(mockTokenRepository).update(token);
  }
}
