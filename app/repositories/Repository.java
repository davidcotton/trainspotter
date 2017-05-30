package repositories;

import com.avaje.ebean.Model;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends Model> {

  /**
   * Fetch all {@link Model}s.
   *
   * @return A collection of {@link Model}.
   */
  List<T> findAll();

  /**
   * Find a {@link Model} via its ID.
   *
   * @param id The ID to search for.
   * @return An optional {@link Model} that might not be found.
   */
  Optional<T> findById(long id);

  /**
   * Insert a {@link Model} into the database.
   *
   * @param model The {@link Model} to insert.
   */
  void insert(T model);

  /**
   * Update given {@link Model} in the database.
   *
   * @param model The {@link Model} to update.
   */
  void update(T model);

  /**
   * Delete a {@link Model} in the database.
   *
   * @param model The {@link Model} to delete.
   */
  void delete(T model);
}
