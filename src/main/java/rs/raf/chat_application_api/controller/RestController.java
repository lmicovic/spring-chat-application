package rs.raf.chat_application_api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@Validated
public interface RestController<T, ID> {

	public ResponseEntity<List<T>> getAll();
	public ResponseEntity<?> getById(ID id);
	public ResponseEntity<T> save(@Valid T entity);
	public ResponseEntity<?> update(ID id, @Valid T entity);
	public ResponseEntity<?> delete(ID id);
	
}
