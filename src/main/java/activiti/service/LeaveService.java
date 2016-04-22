package activiti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import activiti.entity.Leave;
import activiti.repository.hibernate.LeaveDao;
import activiti.util.ConstantUtil;

@Service
@Transactional(readOnly = true)
public class LeaveService {

	@Autowired
	private LeaveDao thisDao;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	public Leave get(String id) {
		return thisDao.get(id);
	}

	@Transactional(readOnly = false)
	public void save(Leave entity) {
		thisDao.save(entity);
	}

	@Transactional(readOnly = false)
	public ProcessInstance startWorkFlow(Leave entity, Map<String, Object> map, String userId) {
		if (null == entity.getId()) {
			entity.setApplyTime(new Date());
			entity.setUserId(userId);
		}
		thisDao.save(entity);
		identityService.setAuthenticatedUserId(userId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ConstantUtil.LEAVE, entity.getId(),
				map);
		entity.setProcessInstanceId(processInstance.getId());// 建立双向关系
		return processInstance;
	}

	public List<Leave> findTasks(String userId) {

		List<Leave> list = new ArrayList<Leave>();
		List<Task> tasks = new ArrayList<Task>();
		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey(ConstantUtil.LEAVE)
				.taskAssignee(userId).list();

		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey(ConstantUtil.LEAVE)
				.taskCandidateUser(userId).list();

		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
					.singleResult();
			String businessKey = instance.getBusinessKey();
			System.out.println("businessKey:" + businessKey);
			Leave leave = get(businessKey);
			leave.setTask(task);
			list.add(leave);
		}
		return list;
	}
}
