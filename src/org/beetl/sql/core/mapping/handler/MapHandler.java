/**  
 * 文件名:    MapHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午11:51:21  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.mapping.BasicRowProcessor;
import org.beetl.sql.core.mapping.ResultSetHandler;
import org.beetl.sql.core.mapping.RowProcessor;


/**  
 * @ClassName: MapHandler   
 * @Description: 将rs处理为Map<String ,Object>  
 * @author: suxj  
 * @param <T>
 * @date:2015年8月2日 上午11:51:21     
 */
public class MapHandler implements ResultSetHandler<java.util.Map<String ,Object>> {
	
	private final RowProcessor convert;
	
	public MapHandler(){
		this(BeanHandler.BASIC_ROW_PROCESSOR);
	}
	
	public MapHandler(NameConversion nc){
		this(new BasicRowProcessor(nc));
	}
	
	public MapHandler(RowProcessor convert){
		super();
		this.convert = convert;
	}

	@Override
	public java.util.Map<String, Object> handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toMap(rs,Map.class) : null;
	}

}
