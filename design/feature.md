特点
===

* SQL 以SQL方式集中管理，数据库SQL能很容易的被集成到开发SQL里，开发SQL也很容易被数据库调试

* 对于简单的单表操作，如更新，插入，以及通过主键，模板查询等，无需SQL

* 将DAO代码里通常的逻辑判断，关系映射 都放到 SQLScript里，用户无需编写java代码

* 跨平台设计
	* 通过组织sql文件，默认在default目录下，跨数据库的sql放在不同目录，在运行的时候选取相应数据库目录
	* 提供一些扩展函数，来屏蔽各个数据库的函数差异


代码例子
===

	List<User>  list = SQLManager.getSQLScript("select").select(paras,User.class);
	User user = SQLManager.getSQLScript(User.class,SELECT_BY_ID).unque(id);
	

API
===

SQLManager
---

* SQLManager.getSQLScript(String id) : 根据id得到SQLScript
* SQLManager.getSQLScript(Class t, int Type) : 根据类型得到SQLScript，type说明如下
	* SELECT_BY_ID,根据主键生成单对象的查询语句，参考SQLScript.unque(Object id)
	* SELECT_BY_TEMPLATE, 根据对象生成一个根据对象里的属性值有无作为条件的SQL，参考SQLScript.selectByTemplate(Object obj)
	* DELETE_BY_ID
	* DELETE_BY_TEMPLATE
	* SELECT_ALL
	* UPDATE_BY_ID
	* UPDATE_BY_TEMPLATE

SQLScript
===

*  SQLScript.unque(Object id)  得到唯一对象，如果有多个，应该抛错
*  SQLScript.query(Map paras,User.class)  根据输入条件查询，结果映射成User类
*  SQLScript.query(Object[] array,User.class)  根据输入条件查询，结果映射成User类,sql 应该都是？号
*  SQLScript.query(Object paras,User.class)  根据输入条件查询.paras 对象的属性转化为map，在调用

*  SQLScript.queryInt(Object paras)  返回一个整形
*  SQLScript.queryLong(Object paras)  返回一个长整形
*  SQLScript.queryArray(Object paras)  返回一个List，List又包含Map
*  SQLScript.queryMap(Object paras)  返回一行，没列放到map里

DataMapper
===
自定义ResultSet到Pojo的映射类
	List<User> list SQLScript.query(Map paras,mapper);




