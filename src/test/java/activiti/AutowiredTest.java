package activiti;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AutowiredTest {

	@Autowired
	private RuntimeService runtimeService;

	@Test
	public void testService() {
		assertNotNull(runtimeService);
	}
}
