package org.beetl.sql.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.beetl.sql.SQLLoader;

import com.mysql.jdbc.Buffer;

/**
 * 从classpath系统加载sql模板，id应该格式是"xx.yyy",xx代表了文件名，yyy代表了sql标识 sql 模板格式如下：
 * 
 * ==selectUser * comment select * from user where .. * ==selectAgenyUser select
 * * from agencyUser where .. *
 * 
 * 
 * @author Administrator
 *
 */
public class ClasspathLoader implements SQLLoader {

    String sqlRoot = null;

	Map<String, SQLSource> sqlSourceMap = new HashMap<String, SQLSource>();

	public ClasspathLoader(String sqlRoot) {
		this.sqlRoot = System.getProperty("user.dir") + File.separator + "bin"
				+ sqlRoot;//这个地址应该通过web容器来获取？？
		File root = new File(this.sqlRoot);
		File[] sqlFiles = root.listFiles();
		for (File file : sqlFiles) {
			loadSql(file);
		}
	}

	@Override
	public SQLSource getSQL(String id) {
		// real path = sqlRoot\xx\yy.sql
		return this.sqlSourceMap.get(id);
	}
	/***
	 * 加载sql文件，并放入sqlSourceMap中
	 * @param file
	 * @return
	 */
	private boolean loadSql(File file) {
		byte[] b = new byte[1024];
		String fileName = file.getName().substring(0,file.getName().lastIndexOf(".")+1);
		LinkedList<String> list = new LinkedList<String>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuffer sql = null;
			String key = null;
			while ((temp = bf.readLine()) != null) {
				if (temp.startsWith("===")) {//读取到===号，说明上一行是key，下面是SQL语句
					if(!list.isEmpty() && list.size() > 1){//如果链表里面有多个，说明是上一句的sql+下一句的key
						String tempKey = list.pollLast();//取出下一句sql的key先存着
						sql = new StringBuffer();
						key = list.pollFirst();
						while (!list.isEmpty()) {//拼装成一句sql
							sql.append(list.pollFirst());
						}
						System.out.println(sql.toString());
						this.sqlSourceMap.put(fileName+key, new SQLSource(sql.toString()));//放入map
						list.addLast(tempKey);//把下一句的key又放进来
					}
				} else {
					list.addLast(temp);
				}
			}
			//最后一句sql
			sql = new StringBuffer();
			key = list.pollFirst();
			while (!list.isEmpty()) {
				sql.append(list.pollFirst());
			}
			this.sqlSourceMap.put(fileName+key, new SQLSource(sql.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
