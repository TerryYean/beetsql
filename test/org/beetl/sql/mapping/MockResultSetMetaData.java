/**  
 * 文件名:    MockResultSetMetaData.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月18日 下午1:29:18  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月18日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.mapping;

/**  
 * @ClassName: MockResultSetMetaData   
 * @Description: ResultSetMetaData在BeanProcessor.mapColumnsToProperties()方法中用到
 * 				 用到了的方法有：geColumnCount()，getColumnLabel(int i)，getColumnName(int i)
 * 				 参考其他开源代码又将hashCode，equals，toString进行了代理
 * @author: suxj  
 * @date:2015年8月18日 下午1:29:18     
 */
public class MockResultSetMetaData {

}
