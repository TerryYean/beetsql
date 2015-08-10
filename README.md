#beetlsql

同时具有Hibernate 优点 & Mybatis优点功能，适用于承认以SQL为中心，同时自动能生成大量常用的SQL的应用

* SQL 以更简洁的方式，Markdown方式集中管理，同时方便程序开发和数据库SQL调试
* SQL 模板基于Beetl实现，更容易写和调试
* 无需注解，自动生成大量内置SQL，轻易完成增删改查功能
* 支持跨数据库平台，开发者所需工作减少到最小
* 具备Interceptor功能，可以调试，性能诊断SQL，以及扩展其他功能



代码例子
===

	List<User>  list = SQLManager.getSQLScript("selectUser").select(paras,User.class);
	User user = SQLManager.getSQLScript(User.class,SELECT_BY_ID).unque(id);

SQL例子
===

	selectUser
	===
		    select * from user where 1=1
		    @if(user.age==1){
		    and age = ${user.age}
		    @}
		    
	selectAll
	===
		    select * from user  
		    @use("selectWhere");
		    
	selectWhere
	===
		    where  age = ${age}
	




