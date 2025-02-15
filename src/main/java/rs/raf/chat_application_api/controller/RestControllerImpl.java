package rs.raf.chat_application_api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import rs.raf.chat_application_api.service.RestService;

public abstract class RestControllerImpl<T, ID> implements RestController<T, ID> {

	protected RestService<T, ID> service;
	
	public RestControllerImpl(RestService<T, ID> service) {
		this.service = service;
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<List<T>> getAll() {
		List<T> entityList = this.service.getAll();
		return new ResponseEntity<List<T>>(entityList, HttpStatus.OK);
	} 
	
	@GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> getById(@PathVariable("id") ID id) {
		
		T entity = this.service.getById(id);
		if(entity == null) {
			return new ResponseEntity<String>("Entity is not found with index: " + id, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<T>(entity, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<T> save(@Valid @RequestBody T entity) {
		T savedEntity = this.service.save(entity);
		return new ResponseEntity<T>(savedEntity, HttpStatus.OK);
	}
	
	@PutMapping(value =  "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> update(@PathVariable("id") ID id, @Valid @RequestBody T entity) {

		T updatedEntity = this.service.update(entity, id);
		if(updatedEntity == null) {
			return new ResponseEntity<String>("Entity is not find with id: " + id, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<T>(updatedEntity, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> delete(@PathVariable("id") ID id) {
		
		T entity = this.service.getById(id);
		if(entity == null) {
			return new ResponseEntity<String>("Entity is not find with id: " + id, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	
	
	
}
