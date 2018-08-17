package bos_management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class StandardTest {

	@Autowired
	private StandardRepository standardRepository;
	
	
	@Autowired
	private CourierRepository courierRepository;
	@Test
	public void saveTest() {
		Standard standard = new Standard();
		standard.setId(10);
		standard.setName("1111");
		standardRepository.save(standard);
	}
	
	@Test
	public void test2() {
		Courier findById = courierRepository.findById(63);
		System.out.println(findById.getName()+findById.getCourierNum());
	}
	

}
