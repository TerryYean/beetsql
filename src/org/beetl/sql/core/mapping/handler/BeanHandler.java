/**  
 * 文件名:    BeanHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午12:24:29  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.mapping.BasicRowProcessor;
import org.beetl.sql.core.mapping.ResultSetHandler;
import org.beetl.sql.core.mapping.RowProcessor;

/**  
 * @ClassName: BeanHandler   
 * @Description: 将rs处理为Pojo
 * @author: suxj  
 * @date:2015年8月2日 上午12:24:29     
 */
public class BeanHandler<T> implements ResultSetHandler<T> {
	
	//默认处理器
	static final RowProcessor BASIC_ROW_PROCESSOR = new BasicRowProcessor();
	
	private final Class<T> type;
	private final RowProcessor convert;
	
	public BeanHandler(Class<T> type) {
        this(type, BASIC_ROW_PROCESSOR);
    }
	
	public BeanHandler(Class<T> type ,NameConversion nc) {
        this(type, new BasicRowProcessor(nc));
    }
	
	public BeanHandler(Class<T> type, RowProcessor convert) {
        this.type = type;
        this.convert = convert;
    }

	@Override
	public T handle(ResultSet rs) throws SQLException {
		return rs.next() ? this.convert.toBean(rs, this.type) : null;
	}

}
