package ratingplayground.aop.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {
	private Log log = LogFactory.getLog(LoggerAspect.class);
	
//	@Before("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void log (JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		System.err.println("*****************" + className + "." + methodName + "()");
	}
	
	@Around("@annotation(org.springframework.transaction.annotation.Transactional)")
	public Object log (ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String methodSignature = className + "." + methodName + "()";
		/*String params = "";
		for (Object obj : joinPoint.getArgs()) {
			if (obj == null)
				params += "null, ";
			else
				params += obj.toString() + ", ";
		}
		if (params.length() > 2)
			params = params.substring(0, params.length() - 2);
		
		log.info(methodSignature + " - start");
		log.info(params + " - start");*/
//		System.err.println(methodSignature + " - start");
		log.info(methodSignature + " - start");
		
		try {
			Object rv = joinPoint.proceed();
			log.info(methodSignature + " - ended successfully");
			return rv;
		} catch (Throwable e) {
			log.error(methodSignature + " - end with error " + e.getClass().getName());
			throw e;
		}
	}
	
//	@Around("@annotation(playground.aop.MyLog) && args(name,..)")
	public Object log (ProceedingJoinPoint joinPoint, String name) throws Throwable {
		log.trace("name: " + name);
		if (name == null || name.length() < 5) {
			throw new RuntimeException("Invalid name: " + name + " - it is too short");
		}
		return joinPoint.proceed();
	}
}

