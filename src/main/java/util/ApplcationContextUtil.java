package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplcationContextUtil {

	public static ApplicationContext getApplicationContext() {
		return new ClassPathXmlApplicationContext(ConstantUtil.applicationFileUrl);
	}

}
