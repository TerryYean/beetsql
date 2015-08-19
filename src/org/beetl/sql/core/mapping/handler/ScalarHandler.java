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
import java.sql.ResultSetMetaData;
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
    
   /* private Object getJavaValue(ResultSet rs) throws SQLException{
    	
//    	取得字段对应的java类型，如：java.lang.String
//    	但是ResultSetMetaData的所有方法参数都是int，这样的话就只能默认取 rsmt.getColumnClassName(1);
//    	也就是说只能支持select id from user这种写法了，只能查询一个字段，应该是满足需求的。
//    	其他查询方式保留，方便使用者单独取出mapping进行开发
//    	ResultSetMetaData rsmt = rs.getMetaData();
//		String a = rsmt.getColumnClassName(1);
    	
    	ResultSetMetaData rsmt = rs.getMetaData();
    	String javaType = rsmt.getColumnClassName(1);
    	
    	
    	return null;
    }
    
    private Object convertType(String javaType, ResultSet rs){
    	
//    	Mysql类型和java类型对照表
//    	http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
    	String[] typeArr = {
    			java.lang.String.class.getName(),
    			java.lang.Boolean.class.getName(),
    			java.lang.Integer.class.getName(),
    			java.lang.Long.class.getName(),
    			java.math.BigInteger.class.getName(),
    			java.lang.Float.class.getName(),
    			java.lang.Double.class.getName(),
    			java.math.BigDecimal.class.getName(),
    			java.sql.Date.class.getName(),
    			java.sql.Timestamp.class.getName(),
    			java.sql.Time.class.getName(),
    			byte[].class.getName()
    	};
    	
    	if(javaType.equals(typeArr[0])){
    		
    	}else if(javaType.equals(typeArr[1])){
    		
    	}else if(javaType.equals(typeArr[2])){
    		
    	}else if(javaType.equals(typeArr[3])){
    		
    	}else if(javaType.equals(typeArr[4])){
    		
    	}else if(javaType.equals(typeArr[5])){
    		
    	}else if(javaType.equals(typeArr[6])){
    		
    	}else if(javaType.equals(typeArr[7])){
    		
    	}else if(javaType.equals(typeArr[8])){
    		
    	}else if(javaType.equals(typeArr[9])){
    		
    	}else if(javaType.equals(typeArr[10])){
    		
    	}else if(javaType.equals(typeArr[11])){
    		
    	}else{
    		return rs.getObject(columnIndex);
    	}
    	
    }*/
}
