/**  
 * 文件名:    MppingException.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月1日 上午8:45:46  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月1日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * @ClassName: MppingException   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月1日 上午8:45:46     
 */
public class MappingException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public MappingException(String message) {
		super(message);
	}
	
	public MappingException(Throwable cause) {
		super(cause);
	}
	
	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}
}
