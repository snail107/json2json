package org.ylj.snail.json.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.ow2.util.base64.Base64;

public class ToolUtil {
	
    /** 
     * prefix of ascii string of native character 
     */  
    private static String PREFIX = "\\u";
	
	/**
	 * @Title: toInt
	 * @Description: TODO(字节数据转int)
	 * @date 2013-3-1 上午10:50:26
	 * @author yang-lj
	 * @param @param bRefArr byte[] aa={0,-112}; return 144
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int byte2Int(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;
		for (int i = bRefArr.length - 1, x = 0; i >= 0; i--, x++) {
			bLoop = bRefArr[x];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}

	
	/**
	 * @Title: int2Byte
	 * @Description: TODO(int转byte,并指定byte字节长度)
	 * @date 2016年9月23日 下午3:46:40 
	 * @author yang-lj
	 * @param dataLen
	 * @param byteLen
	 * @return
	 */
	public static byte[] int2Byte(int dataLen,int byteLen)throws Exception{
		if(byteLen>4){
			throw new Exception("byteLen 不能大于4...");
		}
		byte[] reLen=new byte[byteLen];
		for(int i=reLen.length-1,x=0;i>=0;i--,x++){
			reLen[x] = (byte) ((dataLen >> 8 * i) & 0xff);
		}
		return reLen;
	}


	/**
	 * @Title: native2Ascii
	 * @Description: TODO(字符串转ascii)
	 * @date 2016年6月23日 下午3:57:11 
	 * @author yang-lj
	 * @param str
	 * @return
	 */
    public static String native2Ascii(String str) {  
        char[] chars = str.toCharArray();  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < chars.length; i++) {  
            sb.append(char2Ascii(chars[i]));  
        }  
        return sb.toString();  
    }  
  
    private static String char2Ascii(char c) {  
        if (c > 255) {  
            StringBuilder sb = new StringBuilder();  
            sb.append(PREFIX);  
            int code = (c >> 8);  
            String tmp = Integer.toHexString(code);  
            if (tmp.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(tmp);  
            code = (c & 0xFF);  
            tmp = Integer.toHexString(code);  
            if (tmp.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(tmp);  
            return sb.toString();  
        } else {  
            return Character.toString(c);  
        }  
    }  
  
    /**
     * @Title: ascii2Native
     * @Description: TODO(ascii转字符串)
     * @date 2016年6月23日 下午3:57:37 
     * @author yang-lj
     * @param str
     * @return
     */
    public static String ascii2Native(String str) {  
        StringBuilder sb = new StringBuilder();  
        int begin = 0;  
        int index = str.indexOf(PREFIX);  
        while (index != -1) {  
            sb.append(str.substring(begin, index));  
            sb.append(ascii2Char(str.substring(index, index + 6)));  
            begin = index + 6;  
            index = str.indexOf(PREFIX, begin);  
        }  
        sb.append(str.substring(begin));  
        return sb.toString();  
    }  
  
    private static char ascii2Char(String str) {  
        if (str.length() != 6) {  
            throw new IllegalArgumentException(  
                    "Ascii string of a native character must be 6 character.");  
        }  
        if (!PREFIX.equals(str.substring(0, 2))) {  
            throw new IllegalArgumentException(  
                    "Ascii string of a native character must start with \"\\u\".");  
        }  
        String tmp = str.substring(2, 4);  
        int code = Integer.parseInt(tmp, 16) << 8;  
        tmp = str.substring(4, 6);  
        code += Integer.parseInt(tmp, 16);  
        return (char) code;  
    }
    

	/**
	 * 判断是否为手机号码
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[7])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断是否为手机号码或者上网卡
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isMobileNOOrNetCard(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断是否为邮箱
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 将元为单位的数据转化成分为单位
	 * 
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String yuanToFen(String yuan)throws Exception {
		String fen = "";
		if (yuan == null || yuan == "") {
			fen = "0";
		} else {
			if (NumberUtils.isNumber(yuan)) {
				NumberFormat ft = NumberFormat.getInstance();
				Number nbInput = null;
				try {
					nbInput = ft.parse(yuan);
				} catch (ParseException e) {
					throw e;
				}
				double fInput = nbInput.doubleValue() * 100;
				ft.setGroupingUsed(false);
				ft.setMaximumFractionDigits(0);
				fen = ft.format(fInput);
			} else {
				fen = "0";
			}
		}
		return fen;
	}

	/**
	 * 将分为单位的数据转化成元为单位
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String fenToYuan(String fen) throws Exception{
		String yuan = "";
		if (fen == null || fen == "") {
			yuan = "0.00";
		} else {
			if (NumberUtils.isNumber(fen)) {
				NumberFormat ft = NumberFormat.getInstance();
				Number nbInput = null;
				try {
					nbInput = ft.parse(fen);
				} catch (ParseException e) {
					throw e;
				}
				double fInput = nbInput.doubleValue() / 100.00;
				yuan = new DecimalFormat("#.00").format(fInput);
			} else {
				yuan = "0.00";
			}
		}
		return yuan;
	}

	/**
	 * @Title: joinSlashes
	 * @Description: TODO(为URL拼接需要的斜杠,解决linux与windows平台区分)
	 * @date 2012-11-30 下午05:59:55
	 * @author yang-lj
	 * @param @param url
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String joinSlashes(String url)throws Exception {
		try {
			if (url.lastIndexOf("\\") != -1) {
				return "\\";
			} else if (url.lastIndexOf("/") != -1) {
				return "/";
			}
		} catch (Exception e) {
			throw e;
		}
		return "";
	}

	/**
	 * @Title: generaHexStr
	 * @Description: TODO(字节数组 转十六进制无符号字符串 同 byteTohexStr方法。)
	 * @date 2012-12-12 上午12:12:12
	 * @author yang-lj
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String generaHexStr(byte[] randomByte) {
		String stmp = "";
		StringBuffer strb = new StringBuffer();
		for (int i = 0; i < randomByte.length; i++) {
			stmp = java.lang.Integer.toHexString(randomByte[i] & 0xFF);
			if (stmp.length() == 1) {
				strb.append("0" + stmp);
			} else {
				strb.append(stmp);
			}
		}
		return strb.toString().toUpperCase();
	}

	/**
	 * @Title: byteTohexStr
	 * @Description: TODO(字节转十六进制)
	 * @date 2012-12-20 下午06:19:41
	 * @author yang-lj
	 * @param @param bytes
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String byteTohexStr(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * @Title: hexToByte
	 * @Description: TODO(十六进制 字符串转字节，两位一转 注意：传入十六进制必须为大写字符串)
	 * @date 2012-12-20 上午11:05:19
	 * @author yang-lj
	 * @param @param hexStr
	 * @param @return 设定文件
	 * @return byte[] 返回类型
	 * @throws
	 */
	public static byte[] hexToByte(String hex) {
		hex=hex.toUpperCase();
		int len = (hex.length() / 2);
		byte[] hextobyte = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			hextobyte[i] = (byte) ((byte) "0123456789ABCDEF"
					.indexOf(achar[pos]) << 4 | (byte) "0123456789ABCDEF"
					.indexOf(achar[pos + 1]));
		}
		return hextobyte;
	}
	
	/**
	 * Base64转字节
	 * @param base64Str
	 * @return
	 * @throws Exception
	 */
	public static byte[] base64ToByte(String base64Str)throws Exception{
		return Base64.decode(base64Str.toCharArray());
	}

	/**
	 * 字节转Base64
	 * @param byteStr
	 * @return
	 * @throws Exception
	 */
	public static String byteToBase64(byte[] byteStr)throws Exception{
		String reStr=new String(Base64.encode(byteStr));
		return reStr;
	}

	/**
	 * @Title: hex2byte
	 * @Description: TODO(十六进制转字节)
	 * @date 2013-1-16 下午04:23:16
	 * @author yang-lj
	 * @param @param b
	 * @param @param offset
	 * @param @param len
	 * @param @return 设定文件
	 * @return byte[] 返回类型
	 * @throws
	 */
	private static byte[] hex2byte(byte[] b, int offset, int len) {
		byte[] d = new byte[len];
		for (int i = 0; i < len * 2; i++) {
			int shift = i % 2 == 1 ? 0 : 4;
			d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
		}
		return d;
	}

	/**
	 * @Title: hex2byte
	 * @Description: TODO( 十六进制转字节)
	 * @date 2013-1-16 下午04:24:21
	 * @author yang-lj
	 * @param @param s
	 * @param @return 设定文件
	 * @return byte[] 返回类型
	 * @throws
	 */
	public static byte[] hex2byte(String s) {
		if (s.length() % 2 == 0) {
			return hex2byte(s.getBytes(), 0, s.length() >> 1);
		} else {
			throw new RuntimeException("Uneven number(" + s.length()
					+ ") of hex digits passed to hex2byte.");
		}
	}

	/**
	 * @Title: btyeXOR
	 * @Description: TODO(两字节异或)
	 * @date 2012-12-20 上午11:34:45
	 * @author yang-lj
	 * @param @param byte1
	 * @param @param byte2
	 * @param @return 设定文件
	 * @return byte 返回类型
	 * @throws
	 */
	public static byte btyeXOR(byte byte1, byte byte2) {
		return (byte) (byte1 ^ byte2);
	}

	/**
	 * 从json中取得对应的值
	 * 
	 * @date 2012-11-14
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getStringFromJson(JSONObject jsonObject, String key) {
		if (jsonObject != null && key != null && !key.isEmpty()
				&& jsonObject.containsKey(key)) {
			return jsonObject.getString(key);
		} else {
			return "";
		}
	}

	/**
	 * @Title: changeCharset
	 * @Description: TODO(字符串编码转换的实现方法)
	 * @date 2013-2-7 上午10:22:09
	 * @author yang-lj
	 * @param @param str 待转换编码的字符串
	 * @param @param newCharset 目标编码
	 * @param @return
	 * @param @throws UnsupportedEncodingException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		byte[] bs1=null;
		if (str != null) {
			// 用默认字符编码解码字符串。
			bs1 = str.getBytes();
			// 用新的字符编码生成字符串
			return new String(bs1, newCharset);
		}
		return null;
	}

	/**
	 * @Title: changeCharset
	 * @Description: TODO(字符串编码转换的实现方法)
	 * @date 2013-2-7 上午10:21:41
	 * @author yang-lj
	 * @param @param str 待转换编码的字符串
	 * @param @param oldCharset 原编码
	 * @param @param newCharset 目标编码
	 * @param @return
	 * @param @throws UnsupportedEncodingException 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		byte[] bs=null;
		if (str != null) {
			// 用旧的字符编码解码字符串。解码可能会出现异常。
			bs = str.getBytes(oldCharset);
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * @Title: generaRanByte
	 * @Description: TODO(根据传入长度，生成 随机数byte字节-------尽量避免使用)
	 * @date 2013-1-9 下午04:05:12
	 * @author yang-lj
	 * @param @return 设定文件
	 * @return byte[] 返回类型
	 * @throws
	 */
	public static byte[] generaRanByte(int len) {
		Random ran = new Random();
		byte[] allByte = new byte[256];
		for (int i = -128, k = 0, j = 128; i < j; i++, k++) {
			allByte[k] = (byte) i;
		}
		byte[] randomByte = new byte[len];
		for (int i = 0; i < len; i++) {
			int index = ran.nextInt(255);
			randomByte[i] = allByte[index];
		}
		return randomByte;
	}

	/**
	 * @Title: getRandomChar
	 * @Description: TODO(获得0-9,a-z,A-Z范围的随机数)
	 * @date 2013-10-23 下午12:55:17 
	 * @author yang-lj
	 * @param @param length
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static String getRandomChar(int length) {
		char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
				'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z' };
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(62)]);
		}
		return buffer.toString();
	}

	/**
	 * 将字符编码转换成UTF-8码
	 */
	public static String toUTF_8(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, "UTF-8");
	}

	/**
	 * 将字符编码由GBK转换成UTF-8码
	 */
	public static String convertGBKToUTF8(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, "GBK", "UTF-8");
	}

	/**
	 * 将字符编码由UTF8转换成GBK码
	 */
	public static String convertUTF8ToGBK(String str)
			throws UnsupportedEncodingException {
		return changeCharset(str, "UTF-8", "GBK");
	}

	/**
	 * 将字符编码转换成GBK码
	 */
	public static String toGBK(String str) throws UnsupportedEncodingException {
		return changeCharset(str, "GBK");
	}

	/**
	 * 银行卡号掩码 ****0987
	 * 
	 * @param bankCardNo
	 * @return
	 */
	public static String markBankCardNo(String bankCardNo) {
		if (isEmpty(bankCardNo)) {
			return "";
		}
		String trimBankCardNo = bankCardNo.trim();
		if (trimBankCardNo.length() >= 4) {
			return trimBankCardNo.subSequence(0, 4) + "****"
					+ trimBankCardNo.substring(trimBankCardNo.length() - 4);
		} else {
			return trimBankCardNo;
		}
	}

	/**
	 * 判断字符串是否为空.
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(final String src) {
		if (null == src || "".equals(src)) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: getPLName
	 * @Description: TODO(平台名称)
	 * @date 2013-9-14 下午02:35:20
	 * @author yang-lj
	 * @param @param pf
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getPLName(String pf,Map<String,String> map) {
		String pfName = "";
		if (null != map.get(pf)) {
			pfName = map.get(pf).toString();
		}
		return pfName;
	}
	
	/**
	 * @Title: jsonConvert2Map
	 * @Description: TODO(json转Map)
	 * @date 2013-11-29 下午04:49:31 
	 * @author yang-lj
	 * @param @param jsonStr
	 * @param @return    设定文件
	 * @return Map    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,String> jsonConvert2Map(JSONObject jsonStr){
		HashMap<String,String> map=new HashMap<String,String>();
		Iterator<String>  keys=jsonStr.keys();
		while(keys.hasNext()){
			String key=keys.next();            
			String value=jsonStr.getString(key);
			map.put(key, value);
		}
		return map;
	}
	
	public static HashMap<String,String> jsonStrConvert2Map(String jsonStr){
//		{authCode=, pendingRequest=, operatorId=, keyExchangeSn=0PBl3EI9, changeTime=20140527160100, sessionid=201405191553149120140527160100185, customerSysno=, terminalId=2014051915531491, Time=20140527160100, authState=, nickName=, userId=, authCodeTime=, userName=99999999999, workKey=103273450442bf81927394ed643ac79d0bf771dbad6fd4de, auchAction=, activityTime=, channel=AP01, merchId=, iposId=, appKey=null}
		String str=jsonStr.substring(1, jsonStr.length()-1);
		String[] strArray=str.split("\\,");
		HashMap<String,String> map=new HashMap<String,String>();
		for(int i=0,j=strArray.length;i<j;i++){
			String[] strKV=strArray[i].split("=");
			if(strKV.length>1){
				map.put(strKV[0],strKV[1]);
			}else{
				map.put(strKV[0],"");
			}
		}
		return map;
		
	}

	
	/**
	 * Base64编码
	 * @param data 待编码数据
	 * @return String 编码数据
	 * @throws Exception
	 */
	public static String Base64Encode(String data) throws Exception {
		String reStr="";
		char[] b = Base64.encode(data.getBytes("UTF-8"));
		reStr= new String(b);
		return reStr;
	}

	
	/**
	 * Base64解码
	 * @param data 待解码数据
	 * @return String 解码数据
	 * @throws Exception
	 */
	public static String Base64Decode(String data) throws Exception {
		String reStr="";
		byte[] b = Base64.decode(data.toCharArray());
		reStr =new String(b, "UTF-8");
		return reStr; 
	}
	
	/**
	 * @Title: getStringFromJson
	 * @Description: TODO(从json中取得值)
	 * @date 2015年7月28日 下午3:05:18 
	 * @author yang-lj
	 * @param jsonObject
	 * @param key
	 * @return
	 */
	public static String getJsonVal(JSONObject jsonObject, String key) {
		if (jsonObject != null && key != null && !key.isEmpty()
				&& jsonObject.containsKey(key)) {
			return jsonObject.getString(key).trim();
		} else {
			return "";
		}
	}
	
	/**
	 * @Title: byteTohexStr
	 * @Description: TODO(字节转十六进制)
	 * @date 2012-12-20 下午06:19:41
	 * @author yang-lj
	 * @param @param bytes
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String byteTohex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * @Title: jsonConvert2Map
	 * @Description: TODO(jsonObject转HashMap)
	 * @date 2015年2月15日 下午3:02:50 
	 * @author yang-lj
	 * @param @param jsonStr
	 * @param @return    设定文件
	 * @return HashMap<String,String>    返回类型
	 * @throws
	 */
	public static Map<String,String> json2Map(JSONObject jsonStr){
		TreeMap<String,String> map=new TreeMap<String,String>();
		Iterator<String>  keys=jsonStr.keys();
		while(keys.hasNext()){
			String key=keys.next();            
			String value=jsonStr.getString(key);
			map.put(key, value.equals("[]")?"":value);//xml转json,空节点会转成"[]"
		}
		return map;
	}
	
	/**
	 * @Title: map2Json
	 * @Description: TODO(map转json)
	 * @date 2015年8月3日 上午11:07:52 
	 * @author yang-lj
	 * @param fieldsMap
	 * @return
	 */
	public static JSONObject map2Json(Map<String, String> map){
		TreeMap<String, String> fieldsMap = new TreeMap<String, String>(map);
		JSONObject json=new JSONObject();
		Set<?> keySet=fieldsMap.keySet();
		for(Object key:keySet){
			json.put(key, fieldsMap.get(key));
		}
		return json;
	}
	
	/**
	 * @Title: appendTagVal
	 * @Description: TODO(拼接46域)
	 * @date 2015年10月28日 上午11:04:45 
	 * @author weixl
	 * @param tag,val
	 * @return String
	 */
	public static String appendTagVal(String tag,String val){
		if("".equals(val)){
			return "";
		} else {
			String hexValLen = Integer.toHexString( val.length());
			if(hexValLen.length()==1){
				return new String(hexToByte(tag+"0"+hexValLen))+val;
			} else {
				return new String(hexToByte(tag+hexValLen))+val;
			}
		}
	}
	
	/**
	 * @Title: hideCardNum
	 * @Description: TODO(卡号转换123412****1234 格式)
	 * @date 2015年10月28日 上午11:04:45 
	 * @author yang-lj
	 * @param cardNum
	 * @return
	 */
	public static String hideCardNum(String cardNum){
		String hideStr="";
		int len=cardNum.length();
		String bStr=cardNum.substring(0, 6);
		StringBuffer mStr=new StringBuffer();
		int mLen=cardNum.substring(6, len-4).length();
		String eStr=cardNum.substring(len-4);
		for(int i=0;i<mLen;i++){
			mStr.append("*");
		}
		hideStr+=bStr;
		hideStr+=mStr.toString();
		hideStr+=eStr;
		return  hideStr;
	}
	
	/**
	 * @Title: map2Json
	 * @Description: 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回 
	 * @date 2015年09月29日 上午11:07:52 
	 * @author weixl
	 * @param str 
	 * @param formatLength 
	 * @return 重组后的数据 
	 */
	public static String frontCompWithZore(String str,int formatLength)  {
	    int strLen = str.length();
	    StringBuffer sb = null;
	    while (strLen < formatLength) {
	          sb = new StringBuffer();
	          sb.append("0").append(str);// 左(前)补0
	          str = sb.toString();
	          strLen = str.length();
	    }
	    return str;
	}	
}
