package entities;

import java.security.Key;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jwt_keys")
public class JwsKey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "key_byte_array")
	byte[] key;
	
	public JwsKey(){}
	
	public JwsKey(Key key) {
		this.key = key.getEncoded();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "JwsKey [id=" + id + ", key=" + Arrays.toString(key) + "]";
	}
	
	
}
