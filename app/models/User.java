package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import play.data.validation.Constraints;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class User extends Model {

  enum Status {
    @EnumValue("inactive") inactive,
    @EnumValue("unverified") unverified,
    @EnumValue("active") active,
    @EnumValue("deleted") deleted,
    @EnumValue("banned") banned,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Constraints.Email
  private String email;

  @Constraints.Required
  private String password;

  @Constraints.Required
  private String salt;

  @Constraints.Required
  private String displayName;

  @Enumerated
  private Status status;

  @CreatedTimestamp
  private ZonedDateTime created;
}
