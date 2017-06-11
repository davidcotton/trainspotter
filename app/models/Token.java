package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
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

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Token extends Model {

  private static final int EXPIRATION_PERIOD_HOURS = 4;

  @Getter(onMethod = @__(@JsonIgnore))
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @OneToOne
  private User user;

  @NotNull
  private byte[] hmac;

  @NotNull
  @Column(columnDefinition = "datetime")
  private ZonedDateTime expiry;

  public Token(User user, byte[] hmac, ZonedDateTime expiry) {
    this.user = user;
    this.hmac = hmac;
    this.expiry = expiry;
  }
}
