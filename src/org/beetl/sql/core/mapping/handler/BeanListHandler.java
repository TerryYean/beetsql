package org.beetl.sql.core.mapping.handler;

/**  
 * 文件名:    BeanListHandler.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午10:50:03 
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.beetl.sql.core.mapping.ResultSetHandler;
import org.beetl.sql.core.mapping.RowProcessor;

/**  
 * @ClassName: BeanListHandler   
 * @Description: 将rs处理为Pojo集合  
 * @author: suxj  
 * @date:2015年8月2日 上午10:50:03     
 */
public class BeanListHandler<T> implements ResultSetHandler<List<T>> {

    private final Class<T> type;
    private final RowProcessor convert;

    public BeanListHandler(Class<T> type) {
        this(type, BeanHandler.BASIC_ROW_PROCESSOR);
    }

    public BeanListHandler(Class<T> type, RowProcessor convert) {
        this.type = type;
        this.convert = convert;
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }
}
