/**  
 * 文件名:    BeanHandlerTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月17日 下午4:25:20  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月17日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.mapping.handler;

import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.beetl.sql.core.mapping.ResultSetHandler;
import org.beetl.sql.core.mapping.handler.BeanHandler;
import org.beetl.sql.pojo.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**  
 * @ClassName: BeanHandlerTest   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年8月17日 下午4:25:20     
 */
@RunWith(MockitoJUnitRunner.class)
public class BeanHandlerTest {
	
	@Mock
	private User user;
	@Mock
	private ResultSet rs;
	
	@Before
	public void setUp() throws Exception {
		//MockitoAnnotations.initMocks(this);
		
//		user.setId(1);
//		user.setName("zhangsan");
//		user.setAge(11);
//		user.setUserName("ZHANGSAN");
	}

	@Test
	public void test() {
		ResultSetHandler<User> beanHandler = new BeanHandler<User>(User.class);
		try {
			when(rs.getInt("id")).thenReturn(1);
			when(rs.getString("name")).thenReturn("zhansan");
			when(rs.getInt("age")).thenReturn(11);
			when(rs.getString("userName")).thenReturn("ZHANGSAN");
			
			User _user = beanHandler.handle(rs);
			Assert.assertEquals(user, _user);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
