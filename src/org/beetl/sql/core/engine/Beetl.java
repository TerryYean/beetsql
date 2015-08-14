package org.beetl.sql.core.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.StringTemplateResourceLoader;

public class Beetl {
	private static Beetl beetl = new Beetl();
	GroupTemplate gt = null;
  
	private Beetl() {
		try {
			StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
			Configuration cfg =new Configuration(loadDefaultConfig());
			gt = new GroupTemplate(resourceLoader, cfg);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	/***
	 * 加载cfg自定义配置
	 * 
	 * @param cfg
	 * @return
	 */
	public Properties loadDefaultConfig () {
		Properties ps  = new Properties();
		InputStream ins = this.getClass().getResourceAsStream(
				"/btsql.properties");
		if(ins==null) return ps;
		try {
			ps.load(ins);
		} catch (IOException e) {
			throw new RuntimeException("默认配置文件加载错:/btsql.properties");
		}
		return ps;	
	}

	public static Beetl instance() {
		return beetl;
	}

	public GroupTemplate getGroupTemplate() {
		return gt;
	}
}
