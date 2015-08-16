package org.beetl.sql.core.engine;

import java.io.IOException;

import org.beetl.core.Tag;

public class TrimTag extends Tag {

	@Override
	public void render() {
		try{
			Object[] args = this.args;
			String sql = getBodyContent().getBody();
			if(sql.endsWith(",")){
				this.ctx.byteWriter.writeString(sql.substring(0, sql.length()));
			}else{
				this.ctx.byteWriter.writeString(sql);
			}
		}catch(IOException ie){
			ie.printStackTrace();
			//ignore
		}
		

	}

}
