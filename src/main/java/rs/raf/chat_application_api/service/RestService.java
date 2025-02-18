package rs.raf.chat_application_api.service;

import java.util.List;

public interface RestService <T, ID> {

	public List<T> getAll();
	public T getById(ID id);
	public T save(T object);
	public List<T> saveAll(List<T> objects);
	public T update(T object, ID id);
	public void delete(ID id) throws Exception;
	
}
