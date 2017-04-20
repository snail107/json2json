package org.ylj.snail.json.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @ClassName: Json2Json
 * @Description: TODO(换包为net.sf.json.JSON)
 * @author yanglj
 * @date 2017年4月19日 下午2:37:30
 *
 */
public class Json2Json {
	public static final int TYPE_OBJECT = 0;
	public static final int TYPE_ARRAY = 1;
	public static final int TYPE_PRIMITIVE = 2;

	private Map<String, String> map = new HashMap<String, String>();
	private JSONObject root = new JSONObject();
	
	private boolean subAC_tag=false;//subArrayConvertTag 标识Array里面的objcet存在转换
	private String subAC_nodeName="";//subArrayConvertTag 标识为true时记录Array的KEY名称

	public JSONObject convertJson(JSONObject jo, Map<String, String> map)
			throws Exception {
		this.map=map;
		return convertJson(jo);
	}

	
	private JSONObject convertJson(JSONObject jo) throws Exception {
		if (map == null) {
			Exception e = new Exception("NullPointException:convert map is null");
			throw e;
		}
		root.clear();
		String node = "";
		analyseElement(node,jo,root);
		return JSONObject.fromObject(root);
		
	}

	private void analyseElement(String nodeName,Object reqJe, JSON resJe)
			throws Exception {
//		System.out.println("nodeName:----"+nodeName+"--reqJe:--"+reqJe+"---resJe:---"+resJe);
		int classType = TYPE_PRIMITIVE;
		if (reqJe instanceof JSONObject) {
			nodeName += ".";
			classType = TYPE_OBJECT;
		} else if (reqJe instanceof JSONArray) {
			nodeName += "<>";
			classType = TYPE_ARRAY;
		}
		String name = (String) map.get(nodeName);
		if (name != null) {//只要有配置的都走这
//			System.out.println(nodeName+"----"+name+" root:"+root);
			if(compNodeName(nodeName,name)){//结构发生变动往root里面添加
				insertNode(name,reqJe,root);
			}else{
				insertJe(name,reqJe, resJe);
			}
			return;
		}
		switch (classType) {
		case TYPE_OBJECT:
			JSONObject jo = ((JSONObject) reqJe);
			JSONObject subResjo=null;
			//第一次resJe==root,nodeName=.
			if(root.toString().equals("{}") && nodeName.equals(".")){
				subResjo=(JSONObject)resJe;
			}else{
				subResjo =new JSONObject();
			}
			Iterator<String> it = jo.keySet().iterator();
			try{
			while (it.hasNext()) {
				String key=it.next();
				Object jsone=jo.get(key);
				analyseElement( nodeName + key,jsone,subResjo);
			}
			}catch(Exception e){
				System.out.println("jo:"+jo);
				throw e;
			}
			insertJe(nodeName,subResjo, resJe);
			break;
		case TYPE_ARRAY:
			JSONArray ja = ((JSONArray) reqJe);
			JSONArray subResja=new JSONArray();
			for (int i = 0; i < ja.size(); i++) {
				Object jsone=ja.get(i);
				analyseElement(nodeName,jsone, subResja);
			}
			
			if(subAC_tag){//存在Array里面的object转换
				insertNode(subAC_nodeName,subResja,root);
				subAC_tag=false;
				subAC_nodeName="";
			}else{
				insertJe(nodeName, subResja,resJe);
			}
			break;
		case TYPE_PRIMITIVE:
			insertJe(nodeName,reqJe,resJe);
			break;
		}
	}

	/*
	 * 往root里面添加
	 */
	private void insertNode( String key, Object reqJe,JSONObject resJo)
			throws Exception {
		/*
		 * 升级到一级节点，只有JSONObject可以升级到root
		 * 不能将一个数组升级到root
		 * 不能将一个k/v升级到root 如：.BODY.SJXH:. 只能写成.BODY.SJXH:.SJXH 
		 */
		if(key.equals(".")){
			resJo.putAll((JSONObject)reqJe);
			return;
		}
		try {
			while (true) {
				int pos = key.indexOf(".");
				int arrPos = key.indexOf("<>");
				if (arrPos > 0 && pos > arrPos) {
					pos = -1;
				}
				if (pos >= 0) {
					String k = key.substring(0, pos);					
					key = key.substring(pos + 1, key.length());
					JSONObject tmpNode = (JSONObject) resJo.get(k);
					if (pos==0) tmpNode = root;

					if (tmpNode == null) {
						/*
						 * key为“”传入是
						 */
						if(key.equals("")){
							resJo.put(k, reqJe);
							break;
						}
						tmpNode = new JSONObject();
//						resJo.put(k, tmpNode);//用JsonObject的话在此处add 
						insertNode(key,reqJe,tmpNode);
						resJo.put(k, tmpNode);//用JSONObject的话在此处put??????????
						break;
					} else {
						insertNode(key,reqJe,tmpNode);
						break;
					}
				} else {
					pos = key.indexOf("<>");
					if (pos > 0) {
						String k = key.substring(0, pos);
						String left = key.substring(pos + 2, key.length());
						if (left == null || left.length() == 0) {
							resJo.put(k, reqJe);
							break;
						} else {
							JSONArray tmpArr = (JSONArray) resJo.get(k);
							if (tmpArr == null) {
								tmpArr = new JSONArray();
							}
							if (key.substring(pos + 2, pos + 3).equals(".")) {
								key = key.substring(pos + 3, key.length());
								JSONObject tmpNode = new JSONObject();
								insertNode(key,reqJe,tmpNode);
								tmpArr.add(tmpNode);
							}
							resJo.put(k, tmpArr);
							break;
						}
					} else {
						resJo.put(key, reqJe);
						break;
					}
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			Exception ie = new Exception("Ilegal convert config string");
			throw ie;
		}
	}
	
	/*
	 * 比较待转前后nodeName结构是否有变化 例： 
	 * 只改变key名称返回false
	 * .CZLY.DDDD:.CZLY.DDDD2 返回 false
	 * 改变上一级节点名称返回true
	 * .CZLY.DDDD:.CZLY2.DDDD2 返回 true
	 */
	private boolean compNodeName(String oldNodeStr,String newNodeStr){
		int indexMark=oldNodeStr.lastIndexOf("<>.");//存在array里面的object转换情况，
		/*
		 * 简单判断结构是否一致，不一致返回true 
		 */
		if(indexMark ==-1){
			if(oldNodeStr.split("\\<>").length!=newNodeStr.split("\\<>").length){
				return true;
			}else if(oldNodeStr.split("\\.").length!=newNodeStr.split("\\.").length){
				return true;
			}
		}

		/*
		 * 替换串中的“.”与“<>”，比对节点节构是否有变化
		 */
		String _oldNodeStr=oldNodeStr.replaceAll("\\.","|");
		_oldNodeStr=_oldNodeStr.replaceAll("<>","|");
		String _newNodeStr=newNodeStr.replaceAll("\\.","|");
		_newNodeStr=_newNodeStr.replaceAll("<>","|");
		String[] _oldNodeArray=_oldNodeStr.split("\\|");
		String[] _newNodeArray=_newNodeStr.split("\\|");
		
		/*
		 * 去除最深一层节点名称，不做比较
		 */
		String[] __oldNodeArray=new String[_oldNodeArray.length-1];
		String[] __newNodeArray=new String[_newNodeArray.length-1];
		System.arraycopy(_oldNodeArray, 0, __oldNodeArray, 0, __oldNodeArray.length);
		System.arraycopy(_newNodeArray, 0, __newNodeArray, 0, __newNodeArray.length);
		/*
		 * 2个数组长度均为2，说明都是一级元素
		 */
//		if(_oldNodeArray.length==2 && _newNodeArray.length==2){
//			return false;
//		}
		Arrays.sort(__oldNodeArray);
		Arrays.sort(__newNodeArray);
		if(!Arrays.equals(__oldNodeArray, __newNodeArray)){//转换结构发生了调整
			/*
			 * 并且“<>.”与当前节点在左侧相连，如：<>.YHID 返回false
			 */
//			int indexMark=oldNodeStr.lastIndexOf("<>.");
			int indexField=oldNodeStr.indexOf(_oldNodeArray[_oldNodeArray.length-1]);
			if(indexMark!=-1 && indexField-indexMark==3){
				subAC_tag=true;	
				subAC_nodeName=newNodeStr.substring(0, newNodeStr.lastIndexOf("."));
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/*
	 * 往res里面添加req,节点名称为key
	 */
	private void insertJe( String nodeName,Object req,JSON res) throws Exception{
		if(nodeName.equals(".")){//最后
			return;
		}
		if(req.toString().equals("{}")){//jsonObject的子节点都转换走了只剩下“{}”
			return;
		}
		if(req.toString().equals("[]")){//jsonArray的子节点都转换走了只剩下“[]”
			return;
		}
		if (res instanceof JSONObject ) {
			insertObject(nodeName, req,(JSONObject)res);
		} else if (res instanceof JSONArray ) {
			insertArray(nodeName, req,(JSONArray)res);
		}
	}
	/*
	 * 往JsonObject里面添加元素
	 */
	private void insertObject(String key, Object value,JSONObject jo)
			throws Exception {
		try {
			String keyName=getKeyName(key);
			jo.put(keyName, value);
		} catch (StringIndexOutOfBoundsException e) {
			Exception ie = new Exception("Ilegal convert config string");
			throw ie;
		}
	}
	/*
	 * 往JsonArray里面添加元素
	 */
	private void insertArray(String key, Object value,JSONArray ja)
			throws Exception {
		try {
			int pos = key.indexOf(".");
			int arrPos = key.indexOf("<>");
			if (arrPos > 0 && pos > arrPos) {
				pos = -1;
			}
			String k = key.substring(0, pos);					
			key = key.substring(pos + 1, key.length());
			ja.add(value);
		} catch (StringIndexOutOfBoundsException e) {
			Exception ie = new Exception("Ilegal convert config string");
			throw ie;
		}
	}	
	/*
	 * 取key名称
	 */
	private String getKeyName(String key) {
		if(key.equals("") || key.equals(".") || key.equals("<>")){
			return "";	
		}
		int pos =0;
		int keyLen=key.length();
		int ld=key.lastIndexOf(".")!=-1?key.lastIndexOf(".")+1:0;
		int lk= key.lastIndexOf("<>")!=-1?key.lastIndexOf("<>")+2:0;
		
		if(ld != keyLen && lk != keyLen){//不以./<>结尾
			if(ld > lk){
				pos = ld;
			}else{
				pos = lk;
			}
			return key.substring(pos, key.length());
		}else if( lk == keyLen){//以<>结尾
			return getKeyName(key.substring(0, keyLen-2));
		}else{//以.结尾
			return getKeyName(key.substring(0, keyLen-1));
		}
	}
}

