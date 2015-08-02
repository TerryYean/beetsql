/**  
 * 文件名:    ArrayMatcher.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月2日 下午2:53:08  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月2日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * @ClassName: ArrayMatcher   
 * @Description: 二维数组映射  
 * @author: suxj  
 * @date:2015年8月2日 下午2:53:08     
 */
public class ArrayMatcher implements Matcher {
	
	private java.util.Map<String ,String> map = null;
	
	public ArrayMatcher(String[][] mappingArr){
		
		if(mappingArr == null) throw new IllegalArgumentException("[][] cannot null");
		
		map = new java.util.HashMap<String ,String>();
		for(int i=0 ;i<mappingArr.length ;i++){
			String columnName = mappingArr[i][0];
			if(columnName != null) map.put(columnName.toUpperCase(), mappingArr[i][1]);
		}
		
	}
	
	@Override
	public boolean match(String columnName, String propertyName) {

		if(columnName == null) return false;
		
		String pname = map.get(columnName.toUpperCase());
		if(pname == null) 
			return false;
		else
			return pname.equals(propertyName);
	}
	
//	public static void main(String[] args) {
//		String[][] arr = {{"tId" ,"id"},{"table_name","tableName"},{"table_age","tableAge"}};
//		
//		ArrayMatcher matcher = new ArrayMatcher(arr);
//		System.out.println(matcher.match("tId", "id"));
//	}

}
