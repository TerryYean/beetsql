/**  
 * 文件名:    ProxyFactory.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月18日 上午10:56:50  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月18日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**  
 * @ClassName: ProxyFactory   
 * @Description: 代理工厂，生成JBDC相关的代理类：  
 * 				Connection，Driver， PrepareStatement，
 * 				ResultSet， ResultSetMetaData，Statement
 * @author: suxj  
 * @date:2015年8月18日 上午10:56:50     
 */
public class ProxyFactory {

	/**
	 * 单例
	 */
	private static final ProxyFactory instance = new ProxyFactory();
	private ProxyFactory(){};
	public static ProxyFactory getInstance(){
		return instance;
	}
	
	/**
	 * 
	 * @MethodName: newProxyInstance   
	 * @Description: 先通过java.lang.reflect.Proxy创建handler的动态代理类，
	 * 				  在通过java.lang.Class的cast将动态代理类强制转换成type对象 ，
	 * 				  生成一个期望的代理对象
	 * 				 java.lang.reflect.Proxy API地址及使用方法 : http://tool.oschina.net/apidocs/apidoc?api=jdk-zh
	 * @param @param type
	 * @param @param handler
	 * @param @return  
	 * @return T  
	 * @throws
	 */
	public <T> T newProxyInstance(Class<T> type, InvocationHandler handler){
		/*
		 * 
		 * public static Class<?> getProxyClass(ClassLoader loader, Class<?>... interfaces) throws IllegalArgumentException
		 * loader - 定义代理类的类加载器 
	     * interfaces - 代理类要实现的接口列表 
		 * return - 用指定的类加载器定义的代理类，它可以实现指定的接口 
		 * 
		 *  采用该方式，需要try catch一堆的异常，对于强迫症的我，不会接受的
			Class proxyClass = Proxy.getProxyClass(handler.getClass().getClassLoader(), new Class[]{handler.getClass()});
			Object proxyClassConstructor = proxyClass.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{handler});
			return type.cast(proxyClassConstructor);
		*/
		
		/*
		 * 简写方式：好喜欢这种方式
		 * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) throws IllegalArgumentException
		 * 
		 *  参数：
			    loader - 定义代理类的类加载器 
			    interfaces - 代理类要实现的接口列表 
			    h - 指派方法调用的调用处理程序 
			返回：
		    	一个带有代理类的指定调用处理程序的代理实例，它由指定的类加载器定义，并实现指定的接口 
		 */
		return type.cast(Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class<?>[] {type}, handler));
	}
	
	/**
	 * 
	 * @MethodName: createTargetProxy   
	 * @Description: 创建一个目标代理 : 此处target一般为Driver,ps,rs等
	 * @param @param target 目标类 
	 * @param @param handler	
	 * @param @return  
	 * @return T  
	 * @throws
	 */
	public <T> T createTargetProxy(Class<T> target, InvocationHandler handler){
		
		return this.newProxyInstance(target, handler);
	}
	
	
}
