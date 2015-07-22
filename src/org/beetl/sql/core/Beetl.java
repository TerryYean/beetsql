package org.beetl.sql.core;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

public class Beetl {
	private static Beetl beetl = new Beetl();
	GroupTemplate gt = null;
	private  Beetl(){
		try{
			StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
			Configuration cfg = Configuration.defaultConfiguration();
			cfg.setEngine("org.beetl.sql.core.SQLTemplateEngine");
			cfg.setStatementStart("#");
			cfg.setStatementEnd(null);
			gt = new GroupTemplate(resourceLoader, cfg);
		
		}catch(Exception ex){
			throw new RuntimeException(ex.getMessage());
		}
		
	}
	
	public static Beetl instance(){
		return beetl;
	}
	
	public  GroupTemplate getGroupTemplate(){
		return gt;
	}
}
