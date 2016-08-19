package data;

import java.security.Key;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import entities.JwsKey;

@Transactional
public class JwsKeyDao {
	@PersistenceContext
	EntityManager em;

	public JwsKey create(Key key) {
		System.out.println("IN CREATE");
		JwsKey jwsKey = new JwsKey(key);
		em.persist(jwsKey);
		em.flush();
		return jwsKey;

	}
	
	public Key show(long id) {
		byte[] bytes = em.find(JwsKey.class, id).getKey();
		Key k = new SecretKeySpec(bytes,0,bytes.length,"AES");
		return k;
	}
	
	public List<JwsKey> index() {
		return em.createQuery("SELECT k from JwsKey k").getResultList();
	}
}
