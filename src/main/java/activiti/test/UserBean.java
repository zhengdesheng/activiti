package activiti.test;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.transaction.annotation.Transactional;

public class UserBean {
	/** 由Spring注入 */
	private RuntimeService runtimeService;

	@Transactional
	public void hello() {
		// 这里，你可以在你们的领域模型中做一些事物处理。
		// 当在调用Activiti RuntimeService的startProcessInstanceByKey方法时，
		// 它将会结合到同一个事物中。
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello");

		System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
}
