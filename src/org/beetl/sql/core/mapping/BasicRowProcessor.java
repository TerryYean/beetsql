/**  
 * 文件名:    BasicRowProcessor.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 上午12:35:15  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.beetl.sql.core.NameConversion;

/**  
 * @ClassName: BasicRowProcessor   
 * @Description: 基础rs处理器  
 * @author: suxj  
 * @date:2015年8月2日 上午12:35:15     
 */
public class BasicRowProcessor implements RowProcessor {
	
	private static final BeanProcessor defaultConvert = new BeanProcessor();
	private final BeanProcessor convert;
	
	public BasicRowProcessor(){
		this(defaultConvert);
	}
	
	public BasicRowProcessor(NameConversion nc){
		this(new BeanProcessor(nc));
	}
	
	public BasicRowProcessor(BeanProcessor convert){
		super();
		this.convert = convert;
	}

	@Override
	public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
		return this.convert.toBean(rs, type);
	}

	@Override
	public <T> List<T> toBeanList(ResultSet rs, Class<T> type)
			throws SQLException {
		return this.convert.toBeanList(rs, type);
	}

	@Override
	public Map<String, Object> toMap(ResultSet rs) throws SQLException {
		
		Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
              columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
        
	}
	
	/**
	 * 
	 * @ClassName: CaseInsensitiveHashMap   
	 * @Description: 忽略key大小写  
	 * @author: suxj  
	 * @date:2015年8月2日 下午2:01:23
	 */
	private static class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {

		private static final long serialVersionUID = 9178606903603606031L;
		
		private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

        @Override
        public boolean containsKey(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.containsKey(realKey);
        }

        @Override
        public Object get(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.get(realKey);
        }

        @Override
        public Object put(String key, Object value) {
        	
            /*
             * 保持map和lowerCaseMap同步
             * 在put新值之前remove旧的映射关系
             */
            Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
            Object oldValue = super.remove(oldKey);
            super.put(key, value);
            return oldValue;
        }

        @Override
        public void putAll(Map<? extends String, ?> m) {
            for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                this.put(key, value);
            }
        }

        @Override
        public Object remove(Object key) {
            Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
            return super.remove(realKey);
        }
    }

}
