/**  
 * 文件名:    NumberKit.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年8月24日 上午9:29:34  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年8月24日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.kit;

import java.math.BigDecimal;
import java.math.BigInteger;

/**  
 * @ClassName: NumberKit   
 * @Description: Number工具类
 * @author: suxj  
 * @date:2015年8月24日 上午9:29:34     
 */
public class NumberKit {

	/**
	 * 
	 * @MethodName: convertNumberToTargetClass   
	 * @Description: 将Number转化为期望类型  
	 * @param @param number
	 * @param @param targetClass
	 * @param @return  
	 * @return T  
	 * @throws
	 */
	public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) {
		if(number == null){
			throw new IllegalArgumentException("Number不能为空");
		}
		if(targetClass == null){
			throw new IllegalArgumentException("TargetClass不能为空");
		}
		
		if (targetClass.isInstance(number)) {
			return (T) number;
		}
		else if (Byte.class == targetClass) {
			long value = number.longValue();
			if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
				throw new IllegalArgumentException(number.getClass().getName()+"无法转化为目标对象"+targetClass.getName());
			}
			return (T) new Byte(number.byteValue());
		}
		else if (Short.class == targetClass) {
			long value = number.longValue();
			if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
				throw new IllegalArgumentException(number.getClass().getName()+"无法转化为目标对象"+targetClass.getName());
			}
			return (T) new Short(number.shortValue());
		}
		else if (Integer.class == targetClass) {
			long value = number.longValue();
			if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
				throw new IllegalArgumentException(number.getClass().getName()+"无法转化为目标对象"+targetClass.getName());
			}
			return (T) new Integer(number.intValue());
		}
		else if (Long.class == targetClass) {
			BigInteger bigInt = null;
			if (number instanceof BigInteger) {
				bigInt = (BigInteger) number;
			}
			else if (number instanceof BigDecimal) {
				bigInt = ((BigDecimal) number).toBigInteger();
			}
			// Effectively analogous to JDK 8's BigInteger.longValueExact()
			if (bigInt != null && (bigInt.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0 || bigInt.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) > 0)) {
				throw new IllegalArgumentException(number.getClass().getName()+"无法转化为目标对象"+targetClass.getName());
			}
			return (T) new Long(number.longValue());
		}
		else if (BigInteger.class == targetClass) {
			if (number instanceof BigDecimal) {
				// do not lose precision - use BigDecimal's own conversion
				return (T) ((BigDecimal) number).toBigInteger();
			}
			else {
				// original value is not a Big* number - use standard long conversion
				return (T) BigInteger.valueOf(number.longValue());
			}
		}
		else if (Float.class == targetClass) {
			return (T) new Float(number.floatValue());
		}
		else if (Double.class == targetClass) {
			return (T) new Double(number.doubleValue());
		}
		else if (BigDecimal.class == targetClass) {
			// always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
			// (see BigDecimal javadoc for details)
			return (T) new BigDecimal(number.toString());
		}
		else {
			throw new IllegalArgumentException(number.getClass().getName()+"无法转化为目标对象"+targetClass.getName());
		}
	}
	
	/**
	 * 
	 * @MethodName: parseNumber   
	 * @Description: 将String转化为期望类型  
	 * @param @param text 转化文本
	 * @param @param targetClass 期望类型
	 * @return T  java.lang.Number
	 * @throws
	 */
	public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
		if(text == null){
			throw new IllegalArgumentException("转化值不能为空");
		}
		
		if(targetClass == null){
			throw new IllegalArgumentException("期望类型不能为空");
		}
		
		String trimmed = StringKit.trimAllWhitespace(text);

		if (targetClass.equals(Byte.class)) {
			return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
		}
		else if (targetClass.equals(Short.class)) {
			return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
		}
		else if (targetClass.equals(Integer.class)) {
			return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
		}
		else if (targetClass.equals(Long.class)) {
			return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
		}
		else if (targetClass.equals(BigInteger.class)) {
			return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
		}
		else if (targetClass.equals(Float.class)) {
			return (T) Float.valueOf(trimmed);
		}
		else if (targetClass.equals(Double.class)) {
			return (T) Double.valueOf(trimmed);
		}
		else if (targetClass.equals(BigDecimal.class) || targetClass.equals(Number.class)) {
			return (T) new BigDecimal(trimmed);
		}
		else {
			throw new IllegalArgumentException("无法将["+text+"]转化为期望类型:["+targetClass.getName()+"]");
		}
	}
	
	/**
	 *      Signopt 0x HexDigits 
		    Signopt 0X HexDigits 
		    Signopt # HexDigits 
		    
	 * @MethodName: isHexNumber   
	 * @Description: 是否为十六进制  
	 * @param @param value
	 * @param @return  
	 * @return boolean  
	 * @throws
	 */
	private static boolean isHexNumber(String value) {
		int index = (value.startsWith("-") ? 1 : 0);
		return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
	}
	
	/**
	 * 
	 * @MethodName: decodeBigInteger   
	 * @Description: 解码BigInteger  
	 * @param @param value
	 * @param @return  
	 * @return BigInteger  
	 * @throws
	 */
	private static BigInteger decodeBigInteger(String value) {
		int radix = 10; //进制
		int index = 0;  //脚标
		boolean negative = false; //负标记

		// 处理减
		if (value.startsWith("-")) {
			negative = true;
			index++;
		}

		// 处理进制：16进制，脚标移动2
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
			radix = 16;
		}
		// 处理进制：16进制，脚标移动1
		else if (value.startsWith("#", index)) {
			index++;
			radix = 16;
		}
		// 处理进制：8进制，脚标移动1
		else if (value.startsWith("0", index) && value.length() > 1 + index) {
			index++;
			radix = 8;
		}

		//返回BigInteger
		BigInteger result = new BigInteger(value.substring(index), radix);
		return (negative ? result.negate() : result);
	}
	
}
