package org.ylj.snail.json.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
	
	
   public static void main(String[] args){
//	    	System.out.println(formatTime("2013-03-29 11:04:00"));
//	    	System.out.println(System.nanoTime());	
//	    	System.out.println(formatStringToDate("20121123121618"));
//			String fullTime=TimeUtil.getCurrentTimeMillis();
//			System.out.println(fullTime);
//			System.out.println(fullTime.substring(0,8));	//必须	请求交易日期
//			System.out.println(fullTime.substring(8));		//必须	请求交易流水号
//			System.out.println(fullTime.substring(8,14));//必须	请求交易时间
		
		try {
			System.out.println(TimeUtil.toFormatString("20131112131415"));
			System.out.println(TimeUtil.getTimeDiff("20160127153015"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	   
	/**
	 * @Title: formatDateByFormat
	 * @Description: TODO(传入日期、格式，返回该日期的字符串形式)
	 * @date 2012-11-15 下午04:54:53 
	 * @author yang-lj
	 * @param @param date
	 * @param @param format
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String formatDateByFormat(java.util.Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: formatStringToDate
	 * @Description: TODO(传入日期字符串、格式，返回该字符串的日期形式。)
	 * @date 2012-11-15 下午04:56:40 
	 * @author yang-lj
	 * @param @param dateString
	 * @param @param formatPattern
	 * @param @return
	 * @param @throws java.text.ParseException    设定文件
	 * @return Date    返回类型
	 * @throws
	 */
	public static Date formatStringToDate(String dateString, String formatPattern){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
			return dateFormat.parse(dateString);
		}catch(ParseException pe){
			pe.printStackTrace();
			return null;
		}
    }
	

	/**
	 * @throws Exception 
	 * @Title: toFormatString
	 * @Description: TODO( yyyyMMddHHmmss 转成字符串：格式yyyy-MM-dd HH:mm:ss)
	 * @date 2013-7-20 上午10:48:42 
	 * @author yang-lj
	 * @param @param timeStr
	 * @param @return
	 * @param @throws ParseException    设定文件
	 * @return String    返回类型
	 * @throws
	 */
    public static String toFormatString(String timeStr)throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(formatStringToDate(timeStr));
    }

    /**
     * @Title: toNoFormatString
     * @Description: TODO(格式yyyy-MM-dd HH:mm:ss转成字符串：yyyyMMddHHmmss )
     * @date 2013-12-12 上午11:22:06 
     * @author yang-lj
     * @param @param timeStr
     * @param @return
     * @param @throws ParseException    设定文件
     * @return String    返回类型
     * @throws
     */
//    public static String toNoFormatString(String timeStr)throws ParseException{
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//		return format.format(format.parse(timeStr));
//    }
    

    /**
     * @Title: formatTimeString
     * @Description: TODO(yyyyMMddHHmmss 转成字符串：格式yyyy-MM-dd HH:mm:ss)
     * @date 2013-8-7 上午10:20:02 
     * @author yang-lj
     * @param @param timeStr
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String formatTimeString(String timeStr){
    	StringBuffer sb=new StringBuffer();
    	sb.append(timeStr.substring(0, 4)+"-");
    	sb.append(timeStr.substring(4, 6)+"-");
    	sb.append(timeStr.substring(6, 8)+" ");
    	sb.append(timeStr.substring(8, 10)+":");
    	sb.append(timeStr.substring(10, 12)+":");
    	sb.append(timeStr.substring(12, 14));
    	return sb.toString();
    }
    
    /**
     * @Title: toFormatStringShort
     * @Description: TODO(yyyyMMdd 转成字符串：格式yyyy-MM-dd)   --会报ParseException??
     * @date 2013-7-22 下午02:58:50 
     * @author yang-lj
     * @param @param timeStr
     * @param @return
     * @param @throws ParseException    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String toFormatStringShort(String timeStr)throws ParseException {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	return format.format(format.parse(timeStr));
    }

    
    /**
     * @Title: manuallyFormatStringShort
     * @Description: TODO(手动转换日期 格式,yyyymmdd 格式转为yyyy-mm-dd)
     * @date 2013-7-22 下午03:41:59 
     * @author yang-lj
     * @param @param timeStr
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String manuallyFormatDateStr(String timeStr) {
    	StringBuffer sb=new StringBuffer(timeStr);
		sb.insert(4,"-");
		sb.insert(7,"-");
    	return sb.toString();
    }
    
    
	/**
	 * @Title: formatStringToDate
	 * @Description: TODO(将“yyyyMMddHHmmss” 转换日期)
	 * @date 2012-11-23 下午12:59:07 
	 * @author yang-lj
	 * @param @param dateString
	 * @param @return    设定文件
	 * @return Date    返回类型
	 * @throws
	 */
	public static Date formatStringToDate(String dateString)throws Exception{
		try{
			StringBuffer sb=new StringBuffer(dateString);
			sb.insert(4,"-");
			sb.insert(7,"-");
			sb.insert(10," ");
			sb.insert(13,":");
			sb.insert(16,":");
//			System.out.println("formatStringToDate:"+sb.toString());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(sb.toString());
		}catch(ParseException pe){
			pe.printStackTrace();
			throw pe;
		}
	}
	
	/**
	 * 
	 * @Title: compareDate
	 * @Description: TODO(比较两个日期字符串)
	 * @date 2012-11-15 下午04:59:42 
	 * @author yang-lj
	 * @param @param date1
	 * @param @param date2
	 * @param @return    设定文件
	 * @return int    date1>date2 return 1 ,date1<date2 return -1,date1=date2 return 0
	 * @throws
	 */
/*	public static int compareDate(String date1,String date2){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
		Date dt1=format.parse(date1);
		Date dt2=format.parse(date2);
		if(dt1.getTime()>dt2.getTime()){
			return 1;
		}else if(dt1.getTime()<dt2.getTime()){
			return -1;
		}else{
			return 0;
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}*/
	
	
	/**
	 * @Title: compareDateSupportEmpty
	 * @Description: TODO(比较两个日期字符串)
	 * @date 2013-11-7 上午10:06:40 
	 * @author yang-lj
	 * @param @param date1
	 * @param @param date2
	 * @param @return    设定文件
	 * @return int    返回类型  date1>date2 return 1 ,date1<date2 return -1,date1=date2 return 0
	 * @throws
	 */
	public static int compareDateSupportEmpty(String date1,String date2){
		date1=date1==null?"":date1;
		date2=date2==null?"":date2;
		if(date1.equals("") || date2.equals("")){
			if(date1.equals(date2)){
				return 0;
			}else if(date1.equals("")){
				return -1;
			}else if(date2.equals("")){
				return 1;
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb1=new StringBuffer(date1);
		sb1.insert(4,"-");
		sb1.insert(7,"-");
		StringBuffer sb2=new StringBuffer(date2);
		sb2.insert(4,"-");
		sb2.insert(7,"-");
		try{
			Date dt1=format.parse(sb1.toString());
			Date dt2=format.parse(sb2.toString());
			if(dt1.getTime()>dt2.getTime()){
				return 1;
			}else if(dt1.getTime()<dt2.getTime()){
				return -1;
			}else{
				return 0;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * 
	 * @Title: compareDate
	 * @Description: TODO(比较两个日期字符串)
	 * @date 2012-11-15 下午04:59:42 
	 * @author yang-lj
	 * @param @param date1
	 * @param @param date2
	 * @param @return    设定文件
	 * @return int    date1>date2 return 1 ,date1<date2 return -1,date1=date2 return 0
	 * @throws
	 */
	public static int compareTimeSupportEmpty(String date1,String date2){
		date1=date1==null?"":date1;
		date2=date2==null?"":date2;
		if(date1.equals("") || date2.equals("")){
			if(date1.equals(date2)){
				return 0;
			}else if(date1.equals("")){
				return -1;
			}else if(date2.equals("")){
				return 1;
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb1=new StringBuffer(date1);
		sb1.insert(4,"-");
		sb1.insert(7,"-");
		sb1.insert(10," ");
		sb1.insert(13,":");
		sb1.insert(16,":");
		StringBuffer sb2=new StringBuffer(date2);
		sb2.insert(4,"-");
		sb2.insert(7,"-");
		sb2.insert(10," ");
		sb2.insert(13,":");
		sb2.insert(16,":");
		try{
			Date dt1=format.parse(sb1.toString());
			Date dt2=format.parse(sb2.toString());
			if(dt1.getTime()>dt2.getTime()){
				return 1;
			}else if(dt1.getTime()<dt2.getTime()){
				return -1;
			}else{
				return 0;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	
	

	/**
	 * 
	 * @Title: compareSysTime
	 * @Description: TODO(比较传入时间与系统时间)
	 * @date 2012-11-15 下午05:17:25 
	 * @author yang-lj
	 * @param @param datetime
	 * @param @return   datetime > systime retrun true;
	 * @return boolean    返回类型
	 * @throws
	 */
	public static boolean compareSysTime(Date datetime){
		if(datetime.getTime() > new Date().getTime() ){
			return true;
		}else{
			return false;
		}
	}
	
    /**
     * @Title: getCurrentTimeMillis
     * @Description: TODO(取当前时间yyyyMMddHHmmssSS)
     * @date Mar 13, 2012 5:14:36 PM 
     * @author yang-lj
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getCurrentTimeMillis(){
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSS");
//    	System.out.println(System.nanoTime());			//14位长
//    	System.out.println(System.currentTimeMillis());	//13位长
    	return formatter.format(new Date());
    }
    
    /***
     * @Title: getCurrentTimeFormat
     * @Description: TODO(返回当前时间格式yyyy-MM-dd HH:mm:ss)
     * @date 2013-8-2 下午06:19:38 
     * @author yang-lj
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getCurrentTimeFormat(){
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    	return formatter.format(new Date());
    }
    
    /**
     * @Title: formatTime
     * @Description: TODO(时间格式yyyy-MM-dd-HH:mm:ss 转为yyyyMMddHHmmss)
     * @date 2013-8-2 下午06:26:12 
     * @author yang-lj
     * @param @param timeStr
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String formatTime(String timeStr){
    	timeStr=timeStr.replace("-","");
    	timeStr=timeStr.replace(":","");
    	timeStr=timeStr.replace(" ","");
    	return timeStr;
    }
    
    /**
     * @Title: getCurrentTime
     * @Description: TODO(取当前时间yyyyMMddHHmmss)
     * @date 2012-11-15 下午06:06:58 
     * @author yang-lj
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getCurrentTime(){
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
    	return formatter.format(new Date());
    }
    
    /**
     * @Title: getCurrentTime
     * @Description: TODO(取当前时间yyyyMMdd)
     * @date 2012-11-15 下午06:06:58 
     * @author yang-lj
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
    public static String getCurrentDate(){
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMdd");
    	return formatter.format(new Date());
    }
    
    
	/**
	 * @throws Exception 
	 * @Title: validationTimeOut
	 * @Description: TODO(验证Session、工作密钥、交换密钥、登录令牌，是否超时，计算单位(分))
	 * @date 2012-11-15 下午04:40:30
	 * @author yang-lj
	 * @param @param timeStr 要验证的时间字符串
	 * @param @param computeTime 时间戳
	 * @param @return 设定文件
	 * @return boolean 返回类型 true未超时 、false超时
	 * @throws
	 */
    public static boolean validationTimeOut(String timeStr, String computeTime) throws Exception {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(TimeUtil.formatStringToDate(timeStr));
		calendar.add(Calendar.MINUTE, Integer.valueOf(computeTime));
		return compareSysTime(calendar.getTime());
	}
    
	/**
	 * @Title: validationTimeOutSecond
	 * @Description: TODO(验证Session、工作密钥、交换密钥、登录令牌，是否超时，计算单位(秒))
	 * @date 2012-11-15 下午04:40:30
	 * @author yang-lj
	 * @param @param timeStr 要验证的时间字符串
	 * @param @param computeTime  时间戳
	 * @param @return 设定文件
	 * @return boolean 返回类型 true未超时 、false超时
	 * @throws
	 */
    public static boolean validationTimeOutSecond(String timeStr, String computeTime)throws Exception {
    	Calendar calendar = GregorianCalendar.getInstance();
    	calendar.setTime(TimeUtil.formatStringToDate(timeStr));
    	calendar.add(Calendar.SECOND, Integer.valueOf(computeTime));
    	return compareSysTime(calendar.getTime());
    }
    
    
    /**
     * @Title: validationSysTime
     * @Description: TODO(效验时间差，由终端上的时间戳与服务器时间上下差)
     * @date 2013-3-29 上午09:59:46 
     * @author yang-lj
     * @param @param timeStr
     * @param @param computeTime 时间戳
     * @param @return    设定文件
     * @return boolean    返回类型
     * @throws
     */
    public static boolean validationSysTime(String timeStr, String computeTime)throws Exception {
    	//上传时间
    	Calendar calendar1 = GregorianCalendar.getInstance();
    	calendar1.setTime(TimeUtil.formatStringToDate(timeStr));
    	//系统时间
    	Calendar calendar2 = GregorianCalendar.getInstance();
    	calendar2.setTime(new Date());
    	calendar2.add(Calendar.MINUTE, Integer.valueOf(computeTime));
    	boolean flag=calendar2.after(calendar1);
    	if(!flag) return flag;
    	calendar2.add(Calendar.MINUTE, -Integer.valueOf(computeTime)*2);
    	return calendar1.after(calendar2);
    }
    
    /**
     * @Title: getHHmmssSS
     * @Description: TODO(获取时分秒毫秒)
     * @date 2013-9-5 下午06:01:27 
     * @author yang-lj
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */
	public static String getHHmmssSS(){
    	SimpleDateFormat formatter = new SimpleDateFormat ("HHmmssSS");
    	return formatter.format(new Date());
	}
	
	/**
	 * @Title: getHHmmssSS
	 * @Description: TODO(获取时间HHmmss)
	 * @date 2013-9-5 下午06:03:21 
	 * @author yang-lj
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String getHHmmss(){
		SimpleDateFormat formatter = new SimpleDateFormat ("HHmmss");
		return formatter.format(new Date());
	}
	
	
	/**
	 * @Title: getCurrentDaySurplusTime
	 * @Description: TODO(计算当前天,剩下的时间,单位：秒)
	 * @date 2015年12月1日 下午2:28:24 
	 * @author yang-lj
	 * @return
	 * @throws ParseException 
	 */
	public static int getCurrentDaySurplusTime() throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d1=df.parse(getCurrentTime());
		Date d2=df.parse(getCurrentDate()+"235959");
		long diff = d2.getTime() - d1.getTime();
		return (int)diff/1000;
	}
	
	
	/**
	 * @Title: getCurrentDaySurplusTime
	 * @Description: TODO(传出时间与当前时间的差，单位：秒)
	 * @date 2016年1月27日 下午2:29:36 
	 * @author yang-lj
	 * @param dateStr
	 * @return
	 */
	public static long getTimeDiff(String dateStr)throws Exception{
		long minute=0l;
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try{
			Date d1 = df.parse(getCurrentTime());
			Date d2 = df.parse(dateStr);
			long diff = d1.getTime() - d2.getTime();
//			long days = diff / (1000 * 60 * 60 * 24);
//			minute = diff / (1000 * 60);
			minute = diff / 1000;
		}catch (Exception e){
			throw e;
		}
		return minute;
	}
	
    /**
     * @Title: getCurrentTime
     * @Description: TODO(取当前时间)
     * @date 2015年8月11日 下午2:01:48 
     * @author yang-lj
     * @param formatStr 格式化字符串如：yyyyMMddHHmmssSS
     * @return
     */
    public static String getCurrentTime(String formatStr){
    	SimpleDateFormat formatter = new SimpleDateFormat (formatStr);
    	return formatter.format(new Date());
    }
	
    /**
     * @Title: validationTimeOut1
     * @Description: TODO(超时验证)
     * @date 2015年2月4日 下午5:22:53 
     * @author yang-lj
     * @param @param timeStr
     * @param @param computeTime
     * @param @return    设定文件
     * @return boolean    返回类型
     * @throws
     */
    public static boolean validationTimeOut1(String timeStr, String computeTime)throws Exception {
    	Calendar calendar = GregorianCalendar.getInstance();
    	calendar.setTime(TimeUtil.formatStringToDate(timeStr));
    	calendar.add(Calendar.MINUTE, Integer.valueOf(computeTime));
    	return compareSysTime(calendar.getTime());
    }
 
    
}
