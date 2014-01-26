package eap.comps.orm.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <p> Title: </p>
 * <p> Description: </p>
 * @作者 chiknin@gmail.com
 * @创建时间 
 * @版本 1.00
 * @修改记录
 * <pre>
 * 版本       修改人         修改时间         修改内容描述
 * ----------------------------------------
 * 
 * ----------------------------------------
 * </pre>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseDAO<T, ID extends Serializable> {
	
	@PersistenceContext(unitName="eap_comps_orm_jpa_entityManager")
	protected EntityManager entityManager;
	
	protected JpaRepository jpaRepository;
	
	private Class entityClass;
	
	@PostConstruct
	public void init() {
		entityClass =(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		jpaRepository = new JpaRepository<T, Serializable>(entityClass, entityManager);
	}
}