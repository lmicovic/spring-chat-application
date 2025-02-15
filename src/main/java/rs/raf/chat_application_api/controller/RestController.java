package rs.raf.chat_application_api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;

public interface RestController<T, ID> {

	public ResponseEntity<List<T>> getAll();
	public ResponseEntity<?> getById(ID id);
	public ResponseEntity<T> save(T entity);
	public ResponseEntity<?> update(ID id, T entity);
	public ResponseEntity<?> delete(ID id);
	
}
