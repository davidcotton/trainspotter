package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Token extends Model {

  public static final int TOKEN_EXPIRY_PERIOD_HOURS = 4;

  @Getter(onMethod = @__(@JsonIgnore))
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("user_id")
  @NotNull
  @OneToOne
  private User user;

  @NotNull
  @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private ZonedDateTime expiry;

  @NotNull
  private String signature;

  public Token(User user, ZonedDateTime expiry, String signature) {
    this.user = user;
    this.expiry = expiry;
    this.signature = signature;
  }

  /**
   * Update an existing token.
   *
   * @param expiry    The new expiry.
   * @param signature The new signature.
   */
  public void update(ZonedDateTime expiry, String signature) {
    this.expiry = expiry;
    this.signature = signature;
  }

  /**
   * Check if a supplied token is valid.
   *
   * @param otherExpiry     The expiry of the supplied token
   * @param otherSignature  The signature of the supplied token.
   * @return True if the token is valid else false.
   */
  public boolean isValid(ZonedDateTime otherExpiry, String otherSignature) {
    // check provided expiry & signature match
    if (!(expiry.isEqual(otherExpiry) && signature.equals(otherSignature))) {
      return false;
    }

    // check the token hasn't expired
    return ZonedDateTime.now().isBefore(expiry);
  }
}
