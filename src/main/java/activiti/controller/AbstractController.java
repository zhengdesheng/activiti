package activiti.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import util.ApplcationContextUtil;

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
	}
}
