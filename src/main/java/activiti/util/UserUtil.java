package activiti.util;

import javax.servlet.http.HttpSession;

import org.activiti.engine.identity.User;

public class UserUtil {
	

	
	public static void setUserToSession(HttpSession session,User user){
		session.setAttribute(ConstantUtil.USER, user);
	}
	
	public static User getUserFromSession(HttpSession session){
		Object obj = session.getAttribute(ConstantUtil.USER);
		return obj == null ?null:(User)obj;
	}

}
