package activiti.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import activiti.entity.Leave;
import activiti.service.LeaveService;
import activiti.util.ConstantUtil;
import activiti.util.UserUtil;

@Controller
@RequestMapping(value = "/leave")
public class LeaveController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	public LeaveService thisService;
	
	@Autowired
	public TaskService taskService;

	// @ModelAttribute
	// public Leave get(@RequestParam(required = false) String id) {
	// if (StringUtils.isNotBlank(id)) {
	// return thisService.get(id);
	// } else {
	// return new Leave();
	// }
	// }

	@RequestMapping(value = { "apply", "" })
	public String createForm(Model model) {
		model.addAttribute("leave", new Leave());
		return "/leave/leave-apply";
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public String leaveStart(Leave leave, HttpSession session, RedirectAttributes redirectAttributes, Model modle) {
		try {
			User user = (User) session.getAttribute(ConstantUtil.USER);
			Map<String, Object> map = new HashMap<String, Object>();
			ProcessInstance instance = thisService.startWorkFlow(leave, map, user.getId());
			redirectAttributes.addFlashAttribute("message", "流程启动：，流程ID：" + instance.getId());
		} catch (ActivitiException e) {
			e.printStackTrace();
			if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				logger.debug("没有部署流程", e);
				redirectAttributes.addFlashAttribute("error", "没有部署流程");
			} else {
				logger.debug("启动流程失败", e);
				redirectAttributes.addFlashAttribute("error", "启动流程失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("启动流程失败", e);
			redirectAttributes.addFlashAttribute("error", "启动流程失败");
		}

		return "redirect:/leave/apply";
	}

	@RequestMapping(value = "/task/list", method = RequestMethod.GET)
	public String waitJobs(Model model, HttpSession session) {
		
		User user = UserUtil.getUserFromSession(session);
		if (null != user) {
			List<Leave> list = thisService.findTasks(user.getId());
			System.out.println("list:"+list);
			model.addAttribute("records", list);
			return "leave/leave-task-list";
		} else {
			return "redirect:/leave/leave-apply";
		}

	}

	@RequestMapping(value = "/task/claim", method = RequestMethod.GET)
	public String taskClaim(RedirectAttributes redirects, HttpSession session, @RequestParam("id") String taskId) {
		User user = UserUtil.getUserFromSession(session);

		taskService.claim(taskId, user.getId());
		redirects.addFlashAttribute("message","任务已签收");
		
		return "redirect:/leave/task/list";

	}
	
	@RequestMapping(value = "/task/view", method = RequestMethod.GET)
	public String taskDo( @RequestParam("id") String taskId) {
		 
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		System.out.println("task view:"+task.getTaskDefinitionKey());
		
		return "redirect:/leave/task/list";

	}
}
