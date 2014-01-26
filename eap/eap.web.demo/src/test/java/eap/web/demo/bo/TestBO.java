package eap.web.demo.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import eap.comps.orm.jpa.JpaBaseBO;

@Entity(name="test")
//@Table(name="test")
//@GenericGenerator(name="idGenerator", strategy="eap.comps.orm.jpa.IdGenerator")
public class TestBO extends JpaBaseBO {
	
	private String name;
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="created_time")
	public Date getCreatedTime() {
		return super.getCreatedTime();
	}
}