/**  
 * 文件名:    RowProcessor.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午12:32:47  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**  
 * @ClassName: RowProcessor   
 * @Description: rs处理器接口  
 * @author: suxj  
 * @date:2015年8月2日 上午12:32:47     
 */
public interface RowProcessor {
	
	<T> T toBean(ResultSet rs, Class<T> type) throws SQLException;
	
	<T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;

	Map<String, Object> toMap(ResultSet rs, Class type) throws SQLException;
}
