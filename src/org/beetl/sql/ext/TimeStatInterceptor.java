package org.beetl.sql.ext;

import java.util.Collections;
import java.util.List;

import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.InterceptorContext;

/** 用来统计sql执行时间
 * @author joelli
 *
 */
public class TimeStatInterceptor implements Interceptor {

	List<String> excludes = null;
	long max ;
	public TimeStatInterceptor(long max){
		this(Collections.emptyList(),max);
	}
	public TimeStatInterceptor(List<String> excludes,long max){
		this.excludes = excludes;
	}
	@Override
	public void befor(InterceptorContext ctx) {
		if(excludes.contains(ctx.getSqlId())) return ;
		ctx.put("stat.time", System.currentTimeMillis());

	}

	@Override
	public void after(InterceptorContext ctx) {
		if(excludes.contains(ctx.getSqlId())) return ;
		long end = System.currentTimeMillis();
		long start = (Long)ctx.get("stat.time");
		if((end-start)>max){
			print(ctx.getSqlId(),ctx.getSql(),ctx.getParas());
		}

	}
	
	protected void print(String sqlId,String sql,List<Object> paras){
		System.out.println("sqlId="+sqlId);
		System.out.println("=====================");
		System.out.println(sql);
		
	}

}
