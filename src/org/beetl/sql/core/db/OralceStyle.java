package org.beetl.sql.core.db;

import java.util.List;

public class OralceStyle extends AbstractDBStyle {

		public KeyHolder getKeyHolder(String name) {
			return new OralceKeyHolder(name);
		}
		
		public KeyHolder getKeyHolder(){
			// throw exception or  触发器可以支持？
			return new AutoIncKeyHolder();
		}

		@Override
		public String getPageSQL(String sql) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Object> getPagePara(List<Object> paras, int start, int size) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AbstractDBStyle instance() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		
}
