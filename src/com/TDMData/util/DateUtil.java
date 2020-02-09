package com.TDMData.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil
{

	// List of all date formats that we want to parse.
	// Add your own format here.
	private static List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>()
	{
		private static final long serialVersionUID = 1L;
		{
			add(new SimpleDateFormat("M/dd/yyyy"));
			add(new SimpleDateFormat("dd.M.yyyy"));
			add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("yyyy-dd-MM hh:mm:ss a"));
			add(new SimpleDateFormat("dd.MMM.yyyy"));
			add(new SimpleDateFormat("dd-MMM-yyyy"));
		}
	};
	
	private static List<DateFormat> oracleSqldateFormat = new ArrayList<DateFormat>() {
		{
			//add(new SimpleDateFormat("dd-mon-yyyy hh24:mi:ss"));
			add(new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy"));
			add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"));
		}
	};
	
	
	public static String getOracleInsertDate(String convertDate){ 
		//DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date date = null;
		String strTemp = null;
		String found = "Not Found";
		for(DateFormat formatter : oracleSqldateFormat) {
			
		try {
			date = formatter.parse(convertDate);
			strTemp = dateFormat.format(date);
			found = "Found Data load";
			break;
		} catch (ParseException e1) {
			//e1.printStackTrace();
		}
		}

	//	System.out.println("//////////////////////////////////////////////// found load  result --- > "+found);
		return strTemp;
	}
	
	public static void main(String[] args) {
		String convertDate = "2003-06-17 00:00:00.0";
		
		/*DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
		Date date = null;
		String strTemp = null;
		try {
			date = formatter.parse(convertDate);
			strTemp = dateFormat.format(date);
			//System.out.println(strTemp);
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		System.out.println(getOracleInsertDate(convertDate));
		
		
	/*	//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mon-yyyy hh24:mi:ss");
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		String str = "12-dec-2010 12:34:56";
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		String dateStr = "Mon Jan 01 00:00:00 IST 2007";
		Date date = null;
		try {
			date = formatter.parse(dateStr);
			String strTemp = dateFormat.format(date);
			System.out.println(strTemp);
	//		System.out.println(formatter.parse(dateStr));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
//			Date date = mew 
			//java.sql.Date dt = (java.sql.Date) dateFormat.parse(dateStr);
			//SimpleDateFormat sdf = new SimpleDateFormat("");
		//	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		//System.out.println(sdf.format(sqlDate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/*public String getSqlDate(String dateStr) {
		Date date = null;
		java.sql.Date sqlDate = null;
		String token="";
		for(SimpleDateFormat sqlDateFormat : sqldateFormat) {
			try {
				date = (Date)sqlDateFormat.parse(dateStr);
				sqlDate = new java.sql.Date(date.getTime());
				if(sqlDate != null) {
					break;
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	   // Date d=new Date();
	    return null;
	}*/
	/**
	 * Convert String with various formats into java.util.Date
	 * 
	 * @param input
	 *            Date as a string
	 * @return java.util.Date object if input string is parsed successfully else returns null
	 */
	public static Date convertToDate(String input) {
		Date date = null;
		if (null == input) {
			return null;
		}
		for (SimpleDateFormat format : dateFormats) {
			try {
				format.setLenient(false);
				date = format.parse(input);
			} catch (ParseException e) {
				// Shhh.. try other formats
			}
			if (date != null) {
				break;
			}
		}

		return date;
	}

}