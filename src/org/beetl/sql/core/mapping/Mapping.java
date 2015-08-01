/**  
 * 文件名:    Mapping.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年7月30日 下午11:39:00  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年7月30日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**  
 * @ClassName: Mapping   
 * @Description: 结果集-对象映射  
 * @author: suxj  
 * @date:2015年7月30日 下午11:39:00     
 */
public class Mapping {
	
	private static Mapping mapping = new Mapping(); 
	
	public static Mapping getInstance(){
		return mapping;
	}
	
	/**
	 * 
	 * @MethodName: mapping   
	 * @Description: 单字段返回：比如select count(*) from table;  
	 * @param @param rs
	 * @param @param clazz
	 * @param @return
	 * @param @throws Exception  
	 * @return T  
	 * @throws
	 */
	public <T> T mapping(ResultSet rs ,Class<T> clazz) throws Exception{
		//TODO 最好能限制一下class的类型，用户在提示的时候可以看到，不至于什么类型都能输入
		
		if(null == rs) throw new MappingException("ResultSet 不能为空");
		if(null == clazz) throw new MappingException("Class 不能为空");
		
		List<T> list = mappingList(rs ,clazz);
		
		return (T)list.get(0);
	}

	/**
	 * 
	 * @MethodName: mappingList   
	 * @Description: 将ResultSet映射为pojo对象
	 * @param @param rs
	 * @param @param clazz
	 * @param @return
	 * @param @throws Exception  
	 * @return List<Object>  
	 * @throws
	 */
	public <T> List<T> mappingList(ResultSet rs ,Class<T> clazz) throws Exception{
		
		if(null == rs) throw new MappingException("ResultSet 不能为空");
		if(null == clazz) throw new MappingException("Class 不能为空");
		
		java.util.List<T> listResult = new java.util.ArrayList<T>();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		
		//单列
		if(columnCount == 1){
			while(rs.next()){
				listResult.add((T) rs.getObject(1));
			}
		}
		//多列
		else if(columnCount > 1){
			
			Table table = null;
			//列名-方法
			java.util.HashMap<String ,Method> methodsMap = new java.util.HashMap<String ,Method>();
			
			for(int i=0 ;i<clazz.getDeclaredMethods().length ;i++){
				Method method = new Method();
				String methodName = clazz.getDeclaredMethods()[i].getName();
				
				Class<?>[] paramTypes = clazz.getDeclaredMethods()[i].getParameterTypes();
				method.setMethodName(methodName);
				method.setMethodParamTypes(paramTypes);
				
				methodsMap.put(methodName, method);
			}
			
			table = new Table(columnCount);
			for(int i=0 ;i<columnCount ;i++){
				table.setColumnName(rsMetaData.getColumnName(i+1), i);
	            table.setColumnType(rsMetaData.getColumnType(i+1), i);
			}
			
			while(rs.next()){
				Object obj = parseObjFromResultSet(rs ,table ,clazz ,methodsMap);
				listResult.add((T) obj);
			}
		}

		closeResultSet(rs);
        return listResult;
	}

	/**
	 * 
	 * @MethodName: parseObjFromResultSet   
	 * @Description: 将ResultSet映射为pojo对象
	 * @param @param rs
	 * @param @param table
	 * @param @param clazz
	 * @param @param methodsMap
	 * @param @return
	 * @param @throws Exception  
	 * @return Object  
	 * @throws
	 */
	private Object parseObjFromResultSet(ResultSet rs, Table table,
			Class<?> clazz, HashMap<String, Method> methodsMap) throws Exception {
		
		Object obj = clazz.newInstance();
		java.lang.reflect.Method method = null;
		
		int columnCount = table.getColumnCount();
		String[] columnNames = table.getColumnNames();
		
		for(int i=0 ;i<columnCount ;i++){
			Object columnValue = rs.getObject(columnNames[i]);
			String methodsMapKey = null;
			
			if(columnNames[i] != null){
				methodsMapKey = String.valueOf("set"+toUpperCaseFirstOne(columnNames[i]));
			}
			
			if(methodsMapKey != null){
				Method getMethod = methodsMap.get(methodsMapKey);
				String methodName = getMethod.getMethodName();
				Class<?>[] paramTypes = getMethod.getMethodParamTypes();
				method = clazz.getMethod(methodName, paramTypes);
				
				method.invoke(obj, new Object[]{columnValue});
			}
		}
		
		return obj;
	}
	
	//close resultset
	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}
	
	// 首字母转大写
	private String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

}
