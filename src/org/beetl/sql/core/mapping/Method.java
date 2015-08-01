/**  
 * 文件名:    Method.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年7月30日 下午11:53:33  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年7月30日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * @ClassName: Method   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年7月30日 下午11:53:33     
 */
public class Method {
	
	private String methodName;
	private Class<?>[] methodParamTypes;
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getMethodParamTypes() {
		return methodParamTypes;
	}
	public void setMethodParamTypes(Class<?>[] methodParamTypes) {
		this.methodParamTypes = methodParamTypes;
	}
	
	
}
