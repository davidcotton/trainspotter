package repositories;

import com.avaje.ebean.Model;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Model> {

  /**
   * Fetch all models.
   *
   * @return A collection of models.
   */
  List<T> findAll();

  /**
   * Find a model via its ID.
   *
   * @param id The ID to search for.
   * @return An optional model if found.
   */
  Optional<T> findById(long id);

  /**
   * Insert a model into the database.
   *
   * @param model The model to insert.
   */
  void insert(T model);

  /**
   * Update given model in the database.
   *
   * @param model The model to update.
   */
  void update(T model);
}
