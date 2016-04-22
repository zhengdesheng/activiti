package activiti.controller.identity;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import activiti.util.ConstantUtil;
import activiti.util.UserUtil;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private IdentityService identityService;
	
	@RequestMapping(value="/login")
	public String login(HttpSession session, @RequestParam("username") String userName,
			@RequestParam("password") String password) {
		
	 boolean checkUser = identityService.checkPassword(userName, password);
	 if(checkUser){
		 User user = identityService.createUserQuery().userId(userName).singleResult();
		 UserUtil.setUserToSession(session, user);
		 //获取用户对应的角色
		 List<Group> list =identityService.createGroupQuery().groupMember(user.getId()).list();
		 session.setAttribute(ConstantUtil.GROUP, list);
		 
		 String[] groupNames = new String[list.size()];
		 
		 for (int i=0;i<groupNames.length;i++) {
			 groupNames[i] = list.get(i).getName();
		}
		 session.setAttribute(ConstantUtil.GROUP_NAMES, ArrayUtils.toString(groupNames));
		 return "redirect:/main/index";
	 }else{
		 return "redircet:/login.jsp?error=true";
	 }

	}
	
    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/login";
    }

}
