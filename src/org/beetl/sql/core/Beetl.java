package org.beetl.sql.core;

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
			Configuration cfg = Configuration.defaultConfiguration();
			cfg.setEngine("org.beetl.sql.core.SQLTemplateEngine");
			cfg = loadConfig(cfg);
			gt = new GroupTemplate(resourceLoader, cfg);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}

	/***
	 * 加载cfg自定义配置
	 * 
	 * @param cfg
	 * @return
	 */
	public Configuration loadConfig(Configuration cfg) {
		InputStream ins = this.getClass().getResourceAsStream(
				"/beetl.properties");
		Properties ps = new Properties();
		ClasspathLoader.SYMBOL_BEGIN = "#";
		ClasspathLoader.SYMBOL_END = null;
		if (ins != null) {
			try {
				ps.load(ins);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ClasspathLoader.SYMBOL_BEGIN = ps.getProperty("beetl.SYMBOL_BEGIN");
			ClasspathLoader.SYMBOL_END = ps.getProperty("beetl.SYMBOL_END");
		} else {
			ClasspathLoader.SYMBOL_BEGIN = "#";
			ClasspathLoader.SYMBOL_END = null;
		}
		cfg.setStatementStart(ClasspathLoader.SYMBOL_BEGIN);
		cfg.setStatementEnd(ClasspathLoader.SYMBOL_END);
		if (ClasspathLoader.SYMBOL_END == null) {
			ClasspathLoader.SYMBOL_END = System.getProperty("line.separator","\n");
		}
		return cfg;
	}

	public static Beetl instance() {
		return beetl;
	}

	public GroupTemplate getGroupTemplate() {
		return gt;
	}
}
