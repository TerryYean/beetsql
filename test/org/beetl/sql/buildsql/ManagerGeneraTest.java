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
import org.beetl.sql.core.SQLScript;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.pojo.User;
import org.junit.Before;
import org.junit.Test;

/**  
 * @ClassName: ManagerGeneraTest   
 * @Description: 生成sql模板测试  
 * @author: suxj  
 * @date:2015年8月14日 下午1:52:18     
 */
public class ManagerGeneraTest {
	
	private SQLLoader loader;
	private SQLManager manager;
	
	@Before
	public void before(){
		loader = ClasspathLoader.instance("/sql/mysql");
		manager = new SQLManager(new MySqlStyle(), loader, new MySqlConnectoinSource());
	}

	@Test
	public void test() {
		
		SQLScript script = manager.getScript(User.class,SQLManager.SELECT_BY_ID);
		System.out.println("===SELECT_BY_ID===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.DELETE_BY_ID);
    	System.out.println("===DELETE_BY_ID===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.SELECT_ALL);
    	System.out.println("===SELECT_ALL===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.SELECT_BY_TEMPLATE);
    	System.out.println("===SELECT_BY_TEMPLATE===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.UPDATE_ALL);
    	System.out.println("===UPDATE_ALL===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.UPDATE_BY_ID);
    	System.out.println("===UPDATE_BY_ID===\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.INSERT);
    	System.out.println("===INSERT===\n"+script.getSql());
    	script = manager.getPageSqlScript(manager.getScript(User.class,SQLManager.SELECT_BY_TEMPLATE).getSql());
    	System.out.println("=====page=====\n"+script.getSql());
    	script = manager.getCountSqlScript(manager.getScript(User.class,SQLManager.SELECT_BY_TEMPLATE).getSql());
    	System.out.println("=====count=====\n"+script.getSql());
    	script = manager.getScript(User.class,SQLManager.UPDATE_BY_ID_BATCH);
    	System.out.println("=====batchUpdate=====\n"+script.getSql());
	}

}
