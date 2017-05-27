package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import play.data.validation.ValidationError;
import play.libs.Json;

public class JsonHelper {

  public static final String MESSAGE_NOT_FOUND = "Not found.";
  public static final String MESSAGE_VALIDATION_ERRORS = "Validation errors have occurred.";
  public static final String MESSAGE_UNEXPECTED_ERROR = "An unexpected error has occurred.";

  /**
   * Helper to format error messages as a JSON response.
   *
   * @param message A high level description of the errors that have occurred.
   * @param errors  A collection of fields and their associated error messages.
   */
  public static JsonNode errorsAsJson(String message, Map<String, List<ValidationError>> errors) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode response = mapper.createObjectNode();

    if (StringUtils.isEmpty(message)) {
      message = MESSAGE_UNEXPECTED_ERROR;
    }

    response.put("message", message);

    if (!errors.isEmpty()) {
      Map<String, List<String>> fields = new HashMap<>();
      errors.forEach((key, errs) -> {
        List<String> messages = new ArrayList<>();
        for (ValidationError error : errs) {
          messages.add(error.message());
        }
        fields.put(key, messages);
      });
      JsonNode node = mapper.convertValue(fields, JsonNode.class);
      response.put("fields", node);
    }

    return Json.toJson(response);
  }

  /**
   * Helper to format error messages as a JSON response.
   *
   * @param message A description of the error.
   */
  public static JsonNode errorsAsJson(String message) {
    return errorsAsJson(message, new HashMap<>());
  }

  /**
   * Helper to format error messages as a JSON response.
   *
   * @param errors A collection of fields and their associated error messages.
   */
  public static JsonNode errorsAsJson(Map<String, List<ValidationError>> errors) {
    return errorsAsJson(MESSAGE_VALIDATION_ERRORS, errors);
  }
}
