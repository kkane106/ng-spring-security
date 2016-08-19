package data;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import entities.Todo;
import entities.User;

@Transactional
public class TodoDao {
	@PersistenceContext
	private EntityManager em;
	
	public List<Todo> index(long id) {
		return em.createQuery("SELECT t FROM Todo t WHERE user_id = " + id).getResultList();
	}
	
	public Todo show(long id) {
		return em.find(Todo.class, id);
	}
	
	public boolean update(Todo todo, long id) {
		Todo originalTodo = em.find(Todo.class, id);
		originalTodo.setDescription(todo.getDescription());
		originalTodo.setTitle(todo.getTitle());
		em.merge(originalTodo);
		return true;
	}
	
	public boolean destroy(long id) {
		em.remove(em.find(Todo.class, id));
		return true;
	}
	
	public Todo create(String title, String description, long userId) {
		User u = em.find(User.class, userId);
		Todo t = new Todo(title,description);
		List<Todo> todos = u.getTodos();
		todos.add(t);
		u.setTodos(todos);
		em.merge(u);
		em.flush();
		return t;
	}
}
