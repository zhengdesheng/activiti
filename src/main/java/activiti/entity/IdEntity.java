package activiti.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

 


/**
 * 数据Entity类
 * @author free lance
 * @version 2013-05-28
 */
@MappedSuperclass
public abstract class IdEntity<T>  implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String id;		// 编号
	
	public IdEntity() {
		super();
	}
	
	@PrePersist
	public void prePersist(){
		this.id= UUID.randomUUID().toString().replaceAll("-", "");
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
