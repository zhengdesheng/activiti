package activiti.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import activiti.common.persistence.util.ApplicationContextHelper;
 

public abstract class AbstractController {

	protected ProcessEngine processEngine;
	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected TaskService taskService;
	protected HistoryService historyService;
	protected IdentityService identityService;
	protected ManagementService managementService;
	protected FormService formService;
	
	public AbstractController(){
		processEngine = (ProcessEngine) ApplicationContextHelper.getApplicationContext().getBean("processEngine");
		identityService = (IdentityService) ApplicationContextHelper.getApplicationContext().getBean("identityService");
		repositoryService = (RepositoryService) ApplicationContextHelper.getApplicationContext()
				.getBean("repositoryService");
		runtimeService = (RuntimeService) ApplicationContextHelper.getApplicationContext().getBean("runtimeService");
		taskService = (TaskService) ApplicationContextHelper.getApplicationContext().getBean("taskService");
		historyService = (HistoryService) ApplicationContextHelper.getApplicationContext().getBean("historyService");
		managementService = (ManagementService) ApplicationContextHelper.getApplicationContext()
				.getBean("managementService");
		formService = (FormService) ApplicationContextHelper.getApplicationContext().getBean("formService");
	}
}
