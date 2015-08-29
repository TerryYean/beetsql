package org.beetl.sql.mapping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.beetl.sql.mock.ProxyFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProxyFactoryTest {

	private InvocationHandler handler;
	
	@Before
	public void setUp() throws Exception {
		handler = new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
//				System.out.println(proxy);
				System.out.println(method.getName());
				System.out.println(args);
				return null;
			}
		};
	}

	@Test
	public void testCreateTargetProxy() {
		
//		InvocationHandler.invoke(Object proxy, Method method, Object[] args)是在执行了代理类$Proxy0的相关方法后才被执行
//		当执行到Connection的toString方法的时候，就调用了$Proxy0中的toString（）方法，进而调用父类InvocationHandler.invoke(....)方法
//		动态代理类$Proxy0不仅代理了显示定义的接口中的方法，而且还代理了java的根类Object中的继承而来的equals()、hashcode()、toString()这三个方法，且仅此三个方法。
//		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(Connection.class, handler).toString());
		
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(Connection.class, handler));
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(Driver.class, handler));
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(PreparedStatement.class, handler));
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(ResultSet.class, handler));
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(ResultSetMetaData.class, handler));
		Assert.assertNotNull(ProxyFactory.getInstance().createTargetProxy(Statement.class, handler));
	}

}
