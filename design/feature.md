�ص�
===

* SQL ��SQL��ʽ���й������ݿ�SQL�ܺ����׵ı����ɵ�����SQL�����SQLҲ�����ױ����ݿ����

* ���ڼ򵥵ĵ������������£����룬�Լ�ͨ��������ģ���ѯ�ȣ�����SQL

* ��DAO������ͨ�����߼��жϣ���ϵӳ�� ���ŵ� SQLScript��û������дjava����

* ��ƽ̨���
	* ͨ����֯sql�ļ���Ĭ����defaultĿ¼�£������ݿ��sql���ڲ�ͬĿ¼�������е�ʱ��ѡȡ��Ӧ���ݿ�Ŀ¼
	* �ṩһЩ��չ�����������θ������ݿ�ĺ�������


��������
===

	List<User>  list = SQLManager.getSQLScript("select").select(paras,User.class);
	User user = SQLManager.getSQLScript(User.class,SELECT_BY_ID).unque(id);
	

API
===

SQLManager
---

* SQLManager.getSQLScript(String id) : ����id�õ�SQLScript
* SQLManager.getSQLScript(Class t, int Type) : �������͵õ�SQLScript��type˵������
	* SELECT_BY_ID,�����������ɵ�����Ĳ�ѯ��䣬�ο�SQLScript.unque(Object id)
	* SELECT_BY_TEMPLATE, ���ݶ�������һ�����ݶ����������ֵ������Ϊ������SQL���ο�SQLScript.selectByTemplate(Object obj)
	* DELETE_BY_ID
	* DELETE_BY_TEMPLATE
	* SELECT_ALL
	* UPDATE_BY_ID
	* UPDATE_BY_TEMPLATE

SQLScript
===

*  SQLScript.unque(Object id)  �õ�Ψһ��������ж����Ӧ���״�
*  SQLScript.query(Map paras,User.class)  ��������������ѯ�����ӳ���User��
*  SQLScript.query(Object[] array,User.class)  ��������������ѯ�����ӳ���User��,sql Ӧ�ö��ǣ���
*  SQLScript.query(Object paras,User.class)  ��������������ѯ.paras ���������ת��Ϊmap���ڵ���

*  SQLScript.queryInt(Object paras)  ����һ������
*  SQLScript.queryLong(Object paras)  ����һ��������
*  SQLScript.queryArray(Object paras)  ����һ��List��List�ְ���Map
*  SQLScript.queryMap(Object paras)  ����һ�У�û�зŵ�map��

DataMapper
===
�Զ���ResultSet��Pojo��ӳ����
	List<User> list SQLScript.query(Map paras,mapper);




