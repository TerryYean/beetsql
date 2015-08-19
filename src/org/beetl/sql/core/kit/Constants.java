package org.beetl.sql.core.kit;

public class Constants {
	public static final int SELECT_BY_ID = 0;
	public static final int SELECT_BY_TEMPLATE = 1;
	public static final int SELECT_ACCOUNT_BY_TEMPLATE = 2;	
	public static final int DELETE_BY_ID = 3;
	public static final int SELECT_ALL = 4;
	public static final int UPDATE_ALL = 5;
	public static final int UPDATE_BY_ID = 6;
	//public static final int UPDATE_BY_ID_BATCH =7;
	public static final int INSERT = 8;
	
	public static  String[] classSQL = new String[]{"Auto_SelectById","Auto_SelectByTemplate","Auto_SelectCountByTemplate","Auto_DelById","Auto_SelectAll","Auto_UpdateAll","Auto_UpdateById","Auto_UpdateBatchById","Auto_Insert"};

}
