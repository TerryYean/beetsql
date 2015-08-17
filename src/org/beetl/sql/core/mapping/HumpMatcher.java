/**  
 * 文件名:    HumpMatcher.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 下午2:39:28  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * 扩展：废弃
 * @ClassName: HumpMatcher   
 * @Description: 驼峰映射  比如数据库字段table_name对应pojo字段tbaleName
 * @author: suxj  
 * @date:2015年8月2日 下午2:39:28     
 */
public class HumpMatcher implements Matcher {

	@Override
	public boolean match(String columnName, String propertyName) {
		
		if(columnName == null) return false;
		
		columnName = columnName.toLowerCase();
		String[] array = columnName.split("_");
		StringBuilder sb = new StringBuilder();
		
		for(int i=0 ;i<array.length ;i++){
			String str = array[i];
			if(!"".equals(str) && i>0){
				StringBuilder sb1 = new StringBuilder();
				str = sb1.append(str.substring(0,1).toUpperCase()).append(str.substring(1)).toString();
			}
			sb.append(str);
		}
		return sb.toString().equals(propertyName);

	}
	
//	public static void main(String[] args) {
//		HumpMatcher hump = new HumpMatcher();
//		System.out.println(hump.match("table_name", "tableName"));
//	}

}
