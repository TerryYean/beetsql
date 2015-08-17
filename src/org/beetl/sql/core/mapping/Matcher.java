/**  
 * 文件名:    Matcher.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 下午2:34:20  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * 扩展：废弃
 * @ClassName: Matcher   
 * @Description: 映射器  
 * @author: suxj  
 * @date:2015年8月2日 下午2:34:20     
 */
public interface Matcher {

	/**
	 * 
	 * @MethodName: match   
	 * @Description: 验证字段名与属性名是否匹配  
	 * @param @param columnName 字段名
	 * @param @param propertyName 属性名
	 * @param @return  
	 * @return boolean  
	 * @throws
	 */
	 boolean match(String columnName, String propertyName);
	 
}
