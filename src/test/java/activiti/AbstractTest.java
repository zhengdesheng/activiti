package activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Before;

import util.ApplcationContextUtil;

public class AbstractTest {
	protected ProcessEngine processEngine;
	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected TaskService taskService;
	protected HistoryService historyService;
	protected IdentityService identityService;
	protected ManagementService managementService;
	protected FormService formService;

 
	@Before
	public void setUp() {
		processEngine = (ProcessEngine) ApplcationContextUtil.getApplicationContext().getBean("processEngine");
		identityService = (IdentityService) ApplcationContextUtil.getApplicationContext().getBean("identityService");
		repositoryService = (RepositoryService) ApplcationContextUtil.getApplicationContext()
				.getBean("repositoryService");
		runtimeService = (RuntimeService) ApplcationContextUtil.getApplicationContext().getBean("runtimeService");
		taskService = (TaskService) ApplcationContextUtil.getApplicationContext().getBean("taskService");
		historyService = (HistoryService) ApplcationContextUtil.getApplicationContext().getBean("historyService");
		managementService = (ManagementService) ApplcationContextUtil.getApplicationContext()
				.getBean("managementService");
		formService = (FormService) ApplcationContextUtil.getApplicationContext().getBean("formService");
		//setUserAndGroup();
	}
	
	public void inputStreamBybpmn(String filePath){
		repositoryService.createDeployment().addClasspathResource(filePath).deploy();
	}
	
	public void setUserAndGroup(){
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
		
		assertEquals("zhengdesheng",userInGroup.getId());
		
		Group groupContainZds = identityService.createGroupQuery().groupMember("zhengdesheng").singleResult();
		assertNotNull(groupContainZds);
		assertEquals("deptLeader", groupContainZds.getId());
	}
}
