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
			cfg.setStatementStart("@");
			cfg.setStatementEnd(null);
			cfg = loadConfig(cfg);

			gt = new GroupTemplate(resourceLoader, cfg);
			gt.registerFunction("use", new UseFunction());

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
				"/btsql.properties");
		Properties ps = new Properties();
		String statementStart = "@";
		String statementEnd = null;
		if (ins != null) {
			try {
				ps.load(ins);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			statementStart = ps.getProperty("beetl.SYMBOL_BEGIN");
			statementEnd = ps.getProperty("beetl.SYMBOL_END");
			if (statementEnd == null 
					|| statementEnd.length() == 0
					|| statementEnd.toLowerCase().equals("null")
					|| statementEnd.toLowerCase().equals("\n")
					|| statementEnd.toLowerCase().equals("\r\n"))
				statementEnd = System.getProperty("line.separator", "\n");
		}
		cfg.setStatementStart(statementStart);
		cfg.setStatementEnd(statementEnd);
		return cfg;
	}

	public static Beetl instance() {
		return beetl;
	}

	public GroupTemplate getGroupTemplate() {
		return gt;
	}
}
