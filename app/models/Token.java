package models;

import com.avaje.ebean.Model;
import com.avaje.ebeaninternal.server.lib.util.Str;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import play.Logger;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Token extends Model {

  public static final int TOKEN_EXPIRY_PERIOD_HOURS = 24;

  @Getter(onMethod = @__(@JsonIgnore))
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @OneToOne
  private User user;

  @NotNull
  private byte[] signature;

  @NotNull
  @Column(columnDefinition = "datetime")
  private ZonedDateTime expiry;

  public Token(User user, byte[] signature, ZonedDateTime expiry) {
    this.user = user;
    this.signature = signature;
    this.expiry = expiry;
  }

  public boolean isValid(byte[] other, String sig) {
//    return ZonedDateTime.now().isBefore(getExpiry()) && Arrays.equals(getSignature(), other);

    if (ZonedDateTime.now().isAfter(getExpiry())) {
      return false;
    }
    String sig2 = new String(getSignature());
    Logger.info(sig, sig2);
    boolean result = Arrays.equals(getSignature(), other);
    return result;
  }
}
