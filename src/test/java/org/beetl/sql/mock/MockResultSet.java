package org.beetl.sql.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 
 * @author suxinjie
 *
 */
public class MockResultSet implements InvocationHandler{
	
	//模拟metaData和返回记录，需要在构造函数中初始化
	private ResultSetMetaData metaData = null;
	private Object[] rowArr = null;
	
	public MockResultSet(ResultSetMetaData _metaData, Object[] _rowArr){
		super();
		this.metaData = _metaData;
		this.rowArr = _rowArr;
	}
	
	public static ResultSet createResultSet(ResultSetMetaData _metaData, Object[] _rowArr){
		return ProxyFactory.getInstance().createTargetProxy(ResultSet.class, new MockResultSet(_metaData, _rowArr));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		String methodName = method.getName();
		
		if(methodName.equals("hashCode")){
			return System.identityHashCode(proxy);
		}else if(methodName.equals("toString")){
			return "Mock的ResultSet：hashCode="+System.identityHashCode(proxy);
		}else if(methodName.equals("equals")){
			return proxy == args[0];
		}
		
		//rs.getMetaData()
		//rs.getObject()
		//rs.getString()
		//rs.getInt()
		//rs.getBoolean()
		//rs.getLong()
		//rs.getDouble()
		//rs.getFloat()
		//rs.getShot()
		//rs.getByte()
		//rs.getTimestamp()
		//rs.getSQLXML()
		//rs.next() 要不要pre() ,isFirst(), isLast()?怎么搞？
		else if(methodName.equals("getMetaData")){
			return this.metaData;
		}else if(methodName.equals("getObject")){
			return null;
		}else if(methodName.equals("getString")){
			return null;
		}else if(methodName.equals("getInt")){
			return null;
		}else if(methodName.equals("getBoolean")){
			return null;
		}else if(methodName.equals("getLong")){
			return null;
		}else if(methodName.equals("getDouble")){
			return null;
		}else if(methodName.equals("getFloat")){
			return null;
		}else if(methodName.equals("getShot")){
			return null;
		}else if(methodName.equals("getByte")){
			return null;
		}else if(methodName.equals("getTimestamp")){
			return null;
		}else if(methodName.equals("getSQLXML")){
			return null;
		}else if(methodName.equals("next")){
			return null;
		}
		
		
		
		else{
			throw new Exception("擦~没有mock这个方法："+methodName);
		}
	}

	
	
}
