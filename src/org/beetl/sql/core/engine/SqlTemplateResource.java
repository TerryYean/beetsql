package org.beetl.sql.core.engine;

import java.io.Reader;
import java.io.StringReader;

import org.beetl.core.Resource;
import org.beetl.core.ResourceLoader;
import org.beetl.sql.core.SQLLoader;

public class SqlTemplateResource extends Resource {

	String template = null;
	public SqlTemplateResource(String id, String template,ResourceLoader loader)
	{
		super(id,loader);
		this.template = template ;
	}
	@Override
	public Reader openReader() {
		return new StringReader(template);
	}

	@Override
	public boolean isModified() {
		StringSqlTemplateLoader l = (StringSqlTemplateLoader)this.resourceLoader;
		SQLLoader loader = l.getSqlLLoader();
		return loader.isModified(id);
	}

}
