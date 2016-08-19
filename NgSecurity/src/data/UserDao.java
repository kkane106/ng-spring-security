package data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import entities.User;

@Transactional
public class UserDao {
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	public List<User> index() {
		return em.createQuery("SELECT u FROM User u").getResultList();
	}
	
	public User create(User user) {
		em.persist(user);
		em.flush();
		return user;
	}
	
	public User show(long id) {
		return em.find(User.class, id);
	}
	
	public User authenticateUser(User user) throws NoResultException {
		User u = (User) em.createQuery("SELECT u FROM User u where username = '" + user.getUsername() + "'").getSingleResult();
		if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
			return u;
		}
		return null;
	}
	
}
