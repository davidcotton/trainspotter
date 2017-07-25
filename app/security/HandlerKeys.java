package security;

import be.objectify.deadbolt.java.ConfigKeys;

public enum HandlerKeys {

  DEFAULT(ConfigKeys.DEFAULT_HANDLER_KEY),
  TOKEN("tokenHandler"),
  NO_USER("noUserHandler");

  public final String key;

  HandlerKeys(final String key) {
    this.key = key;
  }
}
