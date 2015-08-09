package org.beetl.sql.ext;

import java.util.Collections;
import java.util.List;

import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.InterceptorContext;

/** 控制台输出sql
 * @author joelli
 *
 */
public class DebugInterceptor implements Interceptor {

	List<String> includes = null;
	public DebugInterceptor(){
	}
	
	public DebugInterceptor(List<String> includes){
		this.includes = includes;
	}
	@Override
	public void befor(InterceptorContext ctx) {
		String sqlId = ctx.getSqlId();
		if(this.isDebugEanble(sqlId)){
			ctx.put("debug.time", System.currentTimeMillis());
		}
		return ;
		

	}

	@Override
	public void after(InterceptorContext ctx) {
		String sqlId = ctx.getSqlId();
		if(this.isDebugEanble(sqlId)){
			long end = System.currentTimeMillis();
			long start = (Long)ctx.get("debug.time");
			long total = end-start;
			print(sqlId,ctx.getSql(),ctx.getParas(),total);
		}
		

	}
	
	protected void print(String sqlId,String sql,List<Object> paras,long time){
		System.out.println("sqlId="+sqlId+" time:"+time);
		System.out.println("=====================");
		System.out.println(sql);
		System.out.println(paras);
		
		
	}
	
	protected boolean isDebugEanble(String sqlId){
		if(this.includes==null) return true;
		for(String id:includes){
			if(sqlId.startsWith(id)){
				return true;
			}
		}
		
		return false;
	}

}
