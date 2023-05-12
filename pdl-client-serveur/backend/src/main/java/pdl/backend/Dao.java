package pdl.backend;

import java.util.Optional;
import java.util.List;

public interface Dao<T> {
  
  void create(final Image img, int getid, int getStatut);

  Optional<T> retrieve(final long id);

  List<T> retrieveAll();

  void update(final T t, final String[] params, int getid, int getStatut);

  void delete(final T t);
}