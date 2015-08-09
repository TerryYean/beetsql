package org.beetl.sql.core;

public interface Interceptor {
	public void befor(InterceptorContext ctx);
	public void after(InterceptorContext ctx);
}
