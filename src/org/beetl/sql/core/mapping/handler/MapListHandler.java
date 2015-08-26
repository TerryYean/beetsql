/**  
 * 文件名:    MapListHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 下午12:06:32  
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
 * @ClassName: MapListHandler   
 * @Description: 将rs处理为List<Map<String ,Object>>  
 * @author: suxj  
 * @date:2015年8月2日 下午12:06:32     
 */
public class MapListHandler implements ResultSetHandler<java.util.List<java.util.Map<String, Object>>> {
	
	private final RowProcessor convert;
	
	public MapListHandler() {
        this(BeanHandler.BASIC_ROW_PROCESSOR);
    }
	
	public MapListHandler(NameConversion nc) {
        this(new BasicRowProcessor(nc));
    }
	
	public MapListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

	@Override
	public java.util.List<java.util.Map<String, Object>> handle(ResultSet rs) throws SQLException {
		
		java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<java.util.Map<String, Object>>();
        while (rs.next()) {
            rows.add(this.convert.toMap(rs,Map.class));
        }
        return rows;
        
	}

}
