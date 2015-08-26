package org.beetl.sql.ext;

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
		print(sqlId,ctx.getSql(),ctx.getParas());
		return ;
		

	}

	@Override
	public void after(InterceptorContext ctx) {
		long time = System.currentTimeMillis();
		long start = (Long)ctx.get("debug.time");
		System.out.println("success:sqlId="+ctx.getSqlId()+",("+(time-start)+")");

	}
	
	protected void print(String sqlId,String sql,List<Object> paras){
		System.out.println("sqlId="+sqlId);
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
