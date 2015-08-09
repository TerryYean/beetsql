package org.beetl.sql.core.kit;

public class StringKit {
	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	// 大写字母前面加上下划线并转为全小写
	public static String enCodeUnderlined(String s) {
		char[] chars = toLowerCaseFirstOne(s).toCharArray();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if(Character.isUpperCase(chars[i])){
				temp.append("_");
			}
			temp.append(Character.toLowerCase(chars[i]));
		}
		return temp.toString();
	}
	// 删除下划线并转把后一个字母转成大写
		public static String deCodeUnderlined(String s) {
			char[] chars = s.toCharArray();
			StringBuilder temp = new StringBuilder();
			for (int i = 0; i < chars.length; i++) {
				if(chars[i] == '_'){
					i++;
					temp.append(Character.toUpperCase(chars[i]));
				}else{
					temp.append(chars[i]);
				}
				
			}
			return temp.toString();
		}
}
