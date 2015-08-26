/**  
 * 文件名:    ResultSetHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午12:22:08  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

import java.sql.SQLException;

/**  
 * @ClassName: ResultSetHandler   
 * @Description: handler接口  
 * @author: suxj  
 * @date:2015年8月2日 上午12:22:08     
 */
public interface ResultSetHandler<T> {

	T handle(java.sql.ResultSet rs) throws SQLException;
}
