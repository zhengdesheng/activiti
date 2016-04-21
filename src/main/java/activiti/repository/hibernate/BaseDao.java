package activiti.repository.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.ognl.SetPropertyAccessor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import activiti.common.persistence.bean.Parameter;
import activiti.common.persistence.util.ReflectHelper;
import activiti.entity.Leave;

 
 

public class BaseDao<T> {

	@Autowired
	private SessionFactory sessionFactory;
	
    /**
     * 实体类类型(由构造方法自动赋值)
     */
    private Class<?> entityClass;

	
	 /**
     * 构造方法，根据实例类自动获取实体类类型
     */
    public BaseDao() {
    	System.out.println("构造函数");
        entityClass = ReflectHelper.getClassGenricType(getClass());
        System.out.println("构造函数实例化的class值是:"+entityClass);
    }

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void save(T entity) {
		try {
			Object id = null;
			for (Method method : entity.getClass().getMethods()) {
				Id idAnns = method.getAnnotation(Id.class);
				if (idAnns != null) {

					id = method.invoke(entity);

					break;
				}
			}
			// 插入前执行方法
			if (StringUtils.isBlank((String) id)) {
				 
				for (Method method : entity.getClass().getMethods()) {
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null) {
						method.invoke(entity);
						break;
					}
				}
			}
			// 更新前执行方法
			else {
				for (Method method : entity.getClass().getMethods()) {
					PreUpdate pu = method.getAnnotation(PreUpdate.class);
					if (pu != null) {
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSession().save(entity);
	}
	
	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		return (T) getSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public T getByHql(String qlString, Parameter parameter) {
		Query query = createQuery(qlString, parameter);
		return (T) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter) {
		Query query = createQuery(qlString, parameter);
		return query.list();
	}

	public Query createQuery(String queryString, Parameter parameter) {
		Query query = getSession().createQuery(queryString);
		setParameter(query, parameter);
		return query;
	}

	public void setParameter(Query query, Parameter parameter) {
		if (null != parameter) {
			Set<String> set = parameter.keySet();
			for (String string : set) {
				Object object = parameter.get(string);
				if (object instanceof Collection<?>) {
					query.setParameterList(string, (Collection<?>) object);
				} else if (object instanceof Object[]) {
					query.setParameterList(string, (Object[]) object);
				} else {
					query.setParameter(string, object);
				}
			}
		}
	}

}
