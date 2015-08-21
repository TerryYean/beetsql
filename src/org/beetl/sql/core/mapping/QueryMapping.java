/**  
 * 文件名:    QueryMapping.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午12:19:23  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

import java.sql.SQLException;

/**  
 * @ClassName: QueryMapping   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月2日 上午12:19:23     
 */
public class QueryMapping {
	
	private QueryMapping(){}
	private static QueryMapping mapping = new QueryMapping();
	public static QueryMapping getInstance(){
		return mapping;
	}

	/**
	 * 
	 * @MethodName: query   
	 * @Description: 通过不同的处理器将rs处理为需要的类型  
	 * @param @param rs 结果
	 * @param @param rsh 结果处理器
	 * @param @return  
	 * @return T  
	 * @throws
	 */
	public <T> T query(java.sql.ResultSet rs ,ResultSetHandler<T> rsh){
		try {
			return rsh.handle(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
