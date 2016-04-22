package activiti.common.persistence.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(false)
public class ApplicationContextHelper implements ApplicationContextAware, DisposableBean {
	private static Logger logger = LoggerFactory.getLogger(ApplicationContextHelper.class);
	private static ApplicationContext applicationContext = null;

	public ApplicationContextHelper() {
		// TODO Auto-generated constructor stub
	}

	public static ApplicationContext getApplicationContext() {
		assertApplicationContext();
		return applicationContext;
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		assertApplicationContext();
		return applicationContext.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) {
		assertApplicationContext();
		return applicationContext.getBean(requiredType);
	}

	@Override
	public void destroy() throws Exception {

	}

	public static void assertApplicationContext() {
		if (null == applicationContext)
			throw new IllegalStateException(
					"applicaitonContext未注入,请在applicationContext.xml中定义ApplicationContextHelper");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {

		logger.error("注入ApplicationContext到SpringContextHolder:" + applicationContext);
		if (ApplicationContextHelper.applicationContext != null)
			logger.info("初始化applicationContext成功");
		ApplicationContextHelper.applicationContext = applicationContext;
	}

}
