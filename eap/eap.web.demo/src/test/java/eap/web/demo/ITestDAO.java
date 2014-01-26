package eap.web.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eap.web.demo.bo.TestBO;

//@Repository("aaaaa")
public interface ITestDAO extends JpaRepository<TestBO, Long> {
	public TestBO findByName(String name);
}
