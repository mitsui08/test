import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.domain.take_delivery.WorkBill;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class Test2 {

	@Autowired
	public WorkBillRepository workBillDao; 
	
	@Test
	public void test3() {
		List<WorkBill> findAll = workBillDao.findAll();
		System.out.println(findAll.size());
		for (WorkBill workBill : findAll) {
			System.out.println(workBill);
		}
	}
}
