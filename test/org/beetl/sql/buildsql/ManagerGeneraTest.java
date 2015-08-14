/**  
 * 文件名:    ManagerGeneraTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月14日 下午1:52:18  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月14日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.buildsql;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.junit.Before;
import org.junit.Test;

/**  
 * @ClassName: ManagerGeneraTest   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月14日 下午1:52:18     
 */
public class ManagerGeneraTest {
	
	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = ClasspathLoader.instance("/sql/mysql");
		manager = new SQLManager();
	}

	@Test
	public void test() {
		
	}

}
