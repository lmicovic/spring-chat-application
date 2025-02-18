package rs.raf.chat_application_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import rs.raf.chat_application_api.configuration.exception.EntityNotFoundException;

public abstract class RestServiceImpl<T, ID> implements RestService<T, ID>{

	protected JpaRepository<T, ID> repository;
	
	public RestServiceImpl(JpaRepository<T, ID> repository) {
		this.repository = repository;
	}
	
	@Override
	public List<T> getAll() {
		return this.repository.findAll();
	}
	
	@Override
	public T getById(ID id) {
		
		Optional<T> findEntity = this.repository.findById(id);
		if(findEntity.isEmpty()) {
			return null;
		}
		
		return findEntity.get();
	}
	
	@Override
	public T save(T object) throws DataIntegrityViolationException {
		return this.repository.save(object);
	}
	
	@Override
	public List<T> saveAll(List<T> objects) throws DataIntegrityViolationException {
		return this.repository.saveAll(objects);
	}
	
	@Override
	public T update(T object, ID id) {
		
		Optional<T> entity = this.repository.findById(id);
		if(entity.isEmpty()) {
			return null;
		}
		
		return this.repository.save(object);
	}
	
	@Override
	public void delete(ID id) throws EntityNotFoundException {
		
		Optional<T> entity = this.repository.findById(id);
		
		if(entity.isEmpty()) {			
			throw new EntityNotFoundException("Entity not found with index: " + id);
		}
		
		this.repository.delete(entity.get());
		
	}
	
}
