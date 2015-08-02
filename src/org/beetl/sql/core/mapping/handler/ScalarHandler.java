/**  
 * 文件名:    ScalarHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 下午13:43:10  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.beetl.sql.core.mapping.ResultSetHandler;

/**  
 * @ClassName: ScalarHandler   
 * @Description: 单值处理器：如select count(*) from user 返回类型为Long
 * @author: suxj  
 * @date:2015年8月2日 下午13:43:10     
 */
public class ScalarHandler<T> implements ResultSetHandler<T> {

    private final int columnIndex;
    private final String columnName;

    public ScalarHandler() {
        this(1, null);
    }

    public ScalarHandler(int columnIndex) {
        this(columnIndex, null);
    }

    public ScalarHandler(String columnName) {
        this(1, columnName);
    }

    private ScalarHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    @SuppressWarnings("unchecked")
	@Override
    public T handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            if (this.columnName == null) return (T) rs.getObject(this.columnIndex);
            return (T) rs.getObject(this.columnName);
        }
        return null;
        
    }
}
