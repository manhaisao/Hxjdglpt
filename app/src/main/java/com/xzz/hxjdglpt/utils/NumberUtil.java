package com.xzz.hxjdglpt.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class NumberUtil
{
	private static final String NUMERIC_CHARS = "0123456789";
	
	private static final DecimalFormat FORMATTER_DECIMAL = new DecimalFormat("0.00");
	
    public static double add(Object obj1, Object obj2)
	{
	    BigDecimal decimal1 = new BigDecimal(toDouble(obj1));
	    BigDecimal decimal2 = new BigDecimal(toDouble(obj2));
	    double result = decimal1.add(decimal2).doubleValue();
	    return Double.valueOf(FORMATTER_DECIMAL.format(result)).doubleValue();
    }
	  
	public static double subtract(Object obj1, Object obj2)
	{
		BigDecimal decimal1 = new BigDecimal(toDouble(obj1));
		BigDecimal decimal2 = new BigDecimal(toDouble(obj2));
		double result = decimal1.subtract(decimal2).doubleValue();
    	return Double.valueOf(FORMATTER_DECIMAL.format(result)).doubleValue();
	}
  
	public static double multiply(Object obj1, Object obj2)
	{
		BigDecimal decimal1 = new BigDecimal(toDouble(obj1));
		BigDecimal decimal2 = new BigDecimal(toDouble(obj2));
		double result = decimal1.multiply(decimal2).doubleValue();
		return Double.valueOf(FORMATTER_DECIMAL.format(result)).doubleValue();
	}
	
	public static double divide(Object obj1, Object obj2)
	{
		double result = 0.0D;
		BigDecimal decimal1 = new BigDecimal(toDouble(obj1));
		BigDecimal decimal2 = new BigDecimal(toDouble(obj2));
		if (toDouble(decimal2) > 0.0D) 
		{
			result = decimal1.divide(decimal2, 3, 1).doubleValue();
		}
		return Double.valueOf(FORMATTER_DECIMAL.format(result)).doubleValue();
	}
	
	public static long round(Object obj)
	{
		BigDecimal decimal = new BigDecimal(toDouble(obj));
		return decimal.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	}
	
    public static final String random(int length)
    {
    	if (length < 1) 
    	{
    		return null;
    	}
    	
    	Random random = new Random();
    	char[] numbers = new char[length];
    	char[] letters = NUMERIC_CHARS.toCharArray();
    	for (int i = 0; i < numbers.length; i++) 
    	{
    		numbers[i] = letters[random.nextInt(10)];
    	}
    	return new String(numbers);
    }
	
	public static Set<Integer> random(int number, int length)
	{
		int size = length;
		if (length > number) 
		{
			size = number;
		}
		
		Random random = new Random();
		Set<Integer> numbers = new TreeSet<Integer>();
		while (numbers.size() < size)
		{
			numbers.add(Integer.valueOf(random.nextInt(number)));
		}
		return numbers;
	}
	
	public static double formatDouble(Object obj, int length)
	{
		String formatter = "0.0";
		for (int i = 1; i < length; i++) 
		{
			formatter = formatter + "0";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat(formatter);
		return Double.valueOf(decimalFormat.format(obj)).doubleValue();
	}
	
	public static short toShort(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Short.parseShort(str);
			}
			catch (Exception ex) {}
		}
		return 0;
	}
	
	public static short toShort1(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Short.parseShort(str);
			}
			catch (Exception ex) {}
		}
		return 1;
	}
	
	public static int toInt(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Integer.parseInt(str);
			}
			catch (Exception ex) {}
		}
		return 0;
	}
	
	public static int toInt1(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Integer.parseInt(str);
			}
			catch (Exception ex) {}
		}
		return 1;
	}
	
	public static int toPage(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				int page = Integer.parseInt(str);
				if (page > 0) 
				{
					return page;
				}
			}
			catch (Exception ex) {}	
		}
		return 1;
	}
	
	public static long toLong(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Long.parseLong(str);
			}
			catch (Exception ex) {}	
		}
		return 0L;
	}
	
	public static float toFloat(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{
				return Float.parseFloat(str);
			}
			catch (Exception ex) {}	
		}
		return 0.0F;
	}
	
	public static double toDouble(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{	
				return Double.parseDouble(str);
			}
			catch (Exception ex) {}	
		}
		return 0.0D;
    }
	
	public static boolean toBoolean(Object obj)
	{
		String str = StringUtil.toString(obj);
		if (!str.equals("")) 
		{
			try 
			{	
				return Boolean.parseBoolean(str);
			}
			catch (Exception ex) {}	
		}
		return false;
	}
}