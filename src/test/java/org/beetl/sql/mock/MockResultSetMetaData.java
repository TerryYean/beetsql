/**  
 * 文件名:    MockResultSetMetaData.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月18日 下午1:29:18  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月18日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.mysql.jdbc.ResultSetMetaData;

/**  
 * @ClassName: MockResultSetMetaData   
 * @Description: ResultSetMetaData在BeanProcessor.mapColumnsToProperties()方法中用到
 * 				 用到了的方法有：geColumnCount()，getColumnLabel(int i)，getColumnName(int i)
 * 				 另外hashCode，equals，toString也要代理，因为代理的时候这3个是默认的，调用这3个方法，会执行
 * 				 代理接口实现类的invoke方法
 * @author: suxj  
 * @date:2015年8月18日 下午1:29:18     
 */
public class MockResultSetMetaData implements InvocationHandler {

	private String[] columnNames = null;
	private String[] columnLabels = null;
	
	/**
	 * 创建一个ResultSetMetaData代理类，用作测试
	 * @param _columnNames
	 * @return
	 */
	public static ResultSetMetaData createResultSetMetaData(String[] _columnNames){
		return ProxyFactory.getInstance().createTargetProxy(ResultSetMetaData.class, new MockResultSetMetaData(_columnNames));
	}
	
	/**
	 * 构造函数
	 * @param _columnnNames
	 */
	public MockResultSetMetaData(String[] _columnnNames){
		super();
		this.columnNames = _columnnNames;
		this.columnLabels = new String[this.columnNames.length];
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		//获取方法名进行，根据调用方法进行return
		String methodName = method.getName();
		
//		低版本java7+才支持switch （String）
//		switch(methodName){
//			case "getColumnCount": return "";
//			case "getColumnName" : return "";
//		}
		
		if(methodName.equals("hashCode")){
			//返回proxy的hashCode，无论proxy是否重写
			return System.identityHashCode(proxy);
		}else if(methodName.equals("toString")){
			//随便输出了一个
			return "Mock的ResultSetMetaData：hashCode="+System.identityHashCode(proxy);
		}else if(methodName.equals("equals")){
			//终于用到Proxy对象了
			return proxy == args[0];
		}
		
//		int getColumnCount() throws SQLException;
//		String getColumnName(int column) throws SQLException;
//		String getColumnLabel(int column) throws SQLException;
//		注意：args具体类型是包装类：java.lang.Boolean,java.lang.Integer
		else if(methodName.equals("getColumnCount")){
			return this.columnNames.length;
		}else if(methodName.equals("getColumnName")){
			Integer index = ((Integer)args[0]).intValue() - 1;
			return this.columnNames[index];
		}else if(methodName.equals("getColumnLabel")){
			Integer index = ((Integer)args[0]).intValue() - 1;
			return this.columnLabels[index];
		}else {
			throw new Exception("擦~没有mock这个方法："+methodName);
		}
	}

}
