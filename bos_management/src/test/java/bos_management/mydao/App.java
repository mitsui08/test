/*package bos_management.mydao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Test;

*//**
 *CREATE BY LIGANG
 *2018-5-3
 *//*
public class App {
	
	public Object createProxyBySpring(){
		
		ClassLoader loader = StandardRepository.class.getClassLoader();
//		Proxy.newProxyINstance(StandardDao.class.getClassLoader(),
//				StandardDao.class.getInterfaces() ---->[IStandardDao.class,IXxxDao.class]
//				new Class[]{IStandardDao.class,IXxxDao.class}--->[IStandardDao.class,IXxxDao.class]
//			);
		Class<?>[] interfaces = new Class[]{StandardRepository.class};
		InvocationHandler h = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				String temp = method.getName();
				if(temp.startsWith("findBy")){
					//System.out.println(temp.substring(6));
					//System.out.println(Standard.class.getSimpleName());
					String hql =" from "+ Standard.class.getSimpleName()+" where "+temp.substring(6).toLowerCase()+"= '"+args[0]+"'";
					System.out.println(hql);
					//执行 jpql jpa query lanugage
				}else{
					//获取方法上的注解
					Annotation[] annotations = method.getAnnotations();
					//获取方法上的@Query注解
					Query query = (Query) annotations[0];
					//获取注解中的value值
					System.out.println(query.value());
					System.out.println(args[0]);
//					Query q = session.createQuery(query.value());
//					q.setParameter(0,args[0]);
//					q.list();
				}
				//执行SimpleJpaRepository.save();
				//Object obj = method.invoke(new SimpleJpaRepository<>(), args);
				//return obj;
				
				return null;
			}
		};
		//最终生成的代理和和StandardRepository接口的关系
		// Proxy implements StandardRepository
		return Proxy.newProxyInstance(loader , interfaces , h );
	}
}
*/