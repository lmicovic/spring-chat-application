package rs.raf.chat_application_api.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;
import rs.raf.chat_application_api.configuration.exception.UnsupportedFunctionException;
import rs.raf.chat_application_api.service.RestService;

@Validated
public abstract class RestControllerImpl<T, K, ID> implements RestController<T, K, ID> {

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
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> getById(@PathVariable("id") ID id) {
		
		T entity = this.service.getById(id);
		if(entity == null) {
			return new ResponseEntity<String>("Entity is not found with index: " + id, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<T>(entity, HttpStatus.OK);
	}
	
	@Deprecated
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> save(@Valid @RequestBody K entity) {
		
		try {
			throw new UnsupportedFunctionException("RestController#save(K) method is not supported, instead create custom save() method implementation");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
//		T savedEntity = this.service.save(entity);
//		return new ResponseEntity<T>(savedEntity, HttpStatus.OK);
	
	}
	
	@Deprecated
	@PostMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> saveAll(@Valid @RequestBody List<K> entities) {
		
		try {
			throw new UnsupportedFunctionException("RestController#saveAll(List<K>) method is not supported, instead create custom saveAll() method implementation");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
//		List<T> savedEntities = this.service.saveAll(entities);
//		return new ResponseEntity<List<T>>(savedEntities, HttpStatus.OK);
	
	}
	
	@Deprecated
	@PutMapping(value =  "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> update(@PathVariable("id") ID id, @Valid @RequestBody K entity) {

		try {
			throw new UnsupportedFunctionException("RestController#update(K) method is not supported, instead create custom update() method implementation");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
//		T updatedEntity = this.service.update(entity, id);
//		if(updatedEntity == null) {
//			return new ResponseEntity<String>("Entity is not find with id: " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		return new ResponseEntity<T>(updatedEntity, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<?> delete(@PathVariable("id") ID id) {
		
		try {
			this.service.delete(id);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Entity is not find with id: " + id, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	
	
	
	
}
