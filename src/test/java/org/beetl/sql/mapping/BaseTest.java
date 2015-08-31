/**  
 * 文件名:    BaseTest.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月18日 上午8:37:11  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月18日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.mapping;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Before;

/**  
 * @ClassName: BaseTest   
 * @Description: 构建测试基类供各测试类使用，在此MockResult，MockResultSetMetaData等
 * 					因为mapping最终会用到这2个类的方法，相应的还要构建一组pojo属性和数据库
 * 					字段的对应关系....
 * 					用Mockito搞了半天，实现起来不是太顺利，决定自己写mock了
 * @author: suxj  
 * @date:2015年8月18日 上午8:37:11     
 */
public class BaseTest {
	
	@Before
	public void setUp(){
		rs = this.createMockResultSet();
		emptyResultSet = this.createMockEmptyResultSet();
	}

	protected static final ResultSetMetaData metaData = null;	//TODO 需要mock一个，供ResultSet使用
	protected ResultSet emptyResultSet = null;
	protected ResultSet rs = null;

	/**
	 * 
	 * @MethodName: createMockResultSet   
	 * @Description: mock一个ResultSet  
	 * @param @return  
	 * @return ResultSet  
	 * @throws
	 */
	protected ResultSet createMockResultSet() {
		//TODO 需要完成
		return null;
	}
	
	/**
	 * 
	 * @MethodName: createMockEmptyResultSet   
	 * @Description: mock一个empty的ResultSet  
	 * @param @return  
	 * @return ResultSet  
	 * @throws
	 */
	protected ResultSet createMockEmptyResultSet(){
		//TODO 需要完成
		return null;
	}
	
}
