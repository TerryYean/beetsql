/**  
 * 文件名:    PojoKit.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月12日 下午5:37:08  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月12日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.kit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  
 * @ClassName: PojoKit   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月12日 下午5:37:08     
 */
public class MapKit {
	
	/**
	 * 
	 * @MethodName: ConvertObjToMap   
	 * @Description: TODO  
	 * @param @param pojo转Map
	 * @param @return  
	 * @return Map  
	 * @throws
	 */
	public static Map<String, Object> convertObjToMap(Object obj) {
		
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null) return null;
		
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = obj.getClass().getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					reMap.put(fields[i].getName(), o);
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reMap;
	}
	
	/**
	 * 将pkNames和value一一对应
	 * @param pkNames
	 * @param value
	 * @return
	 */
	public static Map<String, Object> pksSetValue(List<String> pkNames, Object ...value){
		
		if(pkNames.size() != value.length){
			return null;
		}
		
		Map<String, Object> paras = new HashMap<String, Object>(value.length);
		for(int i=0 ;i<pkNames.size() ;i++){
			paras.put(pkNames.get(i), value[i]);
		}
		
		return paras;
	}

}
