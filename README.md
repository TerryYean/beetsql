#beetlsql

演示如何定制模版引擎，替换beetl核心节点，占位符并不直接输出，而是输出？，并纪录占位符的值
这样，就可以拼凑sql了，能轻易实现mybatis功能

	String sql =" select * from user where id = ${user.id}";
	
	User user = new User();
	user.setId(12);
	Map paras = new HashMap();
	paras.put("user", user);
	
	SQLScript script = new SQLScript("selectUser",sql,paras);
	script.run();
	String jdbcSQL = script.getJdbcSQL();
	List jdbcParas = script.getJDBCParas();
	
	System.out.println(jdbcSQL);
	System.out.println(jdbcParas);
	
结果输出是：

	select * from user where id = ?
	[12]

这样，JDBC工具可以轻易完成MyBatis功能了，具体请参考Test类




