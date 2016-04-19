package activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import util.ApplcationContextUtil;

public class VerySimpleLeaveProcessTest extends AbstractTest {

	@Test
	public void testStartProcess() throws Exception {
		// 创建流程引擎，使用内存数据库
		ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				.buildProcessEngine();
		// 部署流程定义文件
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("sayhelloleave.bpmn").deploy();
		// 验证已部署流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		assertEquals("leavesayhello", processDefinition.getKey());
		// 启动流程并返回流程实例
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello");
		assertNotNull(processInstance);
		System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());
	}

	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

		// RepositoryService repositoryService = (RepositoryService)
		// context.getBean("repositoryService");
		// repositoryService.createDeployment().addClasspathResource("sayhelloleave.bpmn").deploy();

		// 0UserBean userBean = (UserBean) context.getBean("userBean");
		// userBean.hello();
	}

	@Test
	public void testGroupAndUser() {

		super.setUp();
		Group group = identityService.newGroup("deptLeader");
		group.setName("部门领导");
		group.setType("assignment");
		identityService.saveGroup(group);

		User user = identityService.newUser("zhengdesheng");
		user.setFirstName("zheng");
		user.setLastName("desheng");
		user.setEmail("812370410@qq.com");
		identityService.saveUser(user);

		identityService.createMembership("zhengdesheng", "deptLeader");

		User userInGroup = identityService.createUserQuery().memberOfGroup("deptLeader").singleResult();
		assertNotNull(userInGroup);

		assertEquals("zhengdesheng", userInGroup.getId());

		Group groupContainZds = identityService.createGroupQuery().groupMember("zhengdesheng").singleResult();
		assertNotNull(groupContainZds);
		assertEquals("deptLeader", groupContainZds.getId());
	}

	@Test
	public void testUserTask() {

		inputStreamBybpmn("userAndGroupInUserTask.bpmn");
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTask");
		assertNotNull(instance);
		Task task = taskService.createTaskQuery().taskCandidateUser("zhengdesheng").singleResult();
		taskService.claim(task.getId(), "zhengdesheng");
		taskService.complete(task.getId());
	}

	@Test
	public void afterINvokeTestMethod() {
		identityService.deleteMembership("zhengdesheng", "deptLeader");
		identityService.deleteGroup("deptLeader");
		identityService.deleteUser("zhengdesheng");
		// taskService.deleteTask("2509");
		// taskService.deleteTask("45004");
		// taskService.deleteTask("50004");
		// taskService.deleteTask("5009");
		// taskService.deleteTask("7509");
		// taskService.deleteTask("9");
	}

	@Test
	public void testForm() {
		inputStreamBybpmn("leave-bpmn/test.bpmn");
		String start = "E:/MyEclipse Workspaces/activiti/src/main/resources/leave-bpmn/leave-start.form";
		String end ="E:/MyEclipse Workspaces/activiti/src/main/resources/leave-bpmn/approve-deptLeader.form";
		repositoryService.createDeployment().addString("start", getStringForFile(start)).deploy();
		repositoryService.createDeployment().addString("end", getStringForFile(end)).deploy();
		List<ProcessDefinition> processList =  repositoryService.createProcessDefinitionQuery().list();
		for (int i = 0; i < processList.size(); i++) {
			ProcessDefinition process = processList.get(i);
			Object startFrom = formService.getRenderedStartForm(process.getId());
			System.out.println("表单："+startFrom);
		}


	}

	public String getStringForFile(String file) {

		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result = result  + s;
			}
			br.close();
		} catch (Exception e) {
			System.out.println("出现错误");
			e.printStackTrace();
		}
		return result;

	}
	
	 

}
