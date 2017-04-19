package org.ylj.snail.json.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.ylj.snail.json.util.ToolUtil;

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

	public static void main(String args[]) {
		Json2Json tj = new Json2Json();
		/**
		 * 转换范围说明
		 * 一级k/v保持原样 ZZZZ:9999
		 * 一级数组元素保持原样YHID:[\"AA\",\"BB\"]
		 * 一级对象数组保持原样YHKB:[{YYYY:7777},{WWWW:8888}]
		 * 二级K/V保持原样CZLY:{CCCC:333}
		 * 一二级对象KEY名称转换 CZLY:{DDDD:{FFFF:4444} 转换为"CZLY2":{"DDDD2":{"FFFF":4444}}
		 * 二级k/v转换到新的一级对象里面CZLY:{EEEE:5555}转换为"BODY":{"EEEE":5555}
		 * 三级key名称CZLY:{GGGG:{HHHH:6666}}转换为"CZLY":{"GGGG":{"HHHH2":6666}}
		 * 升级INFO节点中的所有k/v到一级节点INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"} 转换为 {"GGGG":"02","WKBH":"b8:bc:1b:09:52:f8"}
		 * 降级k/v到BODY节点中"BODY":{"XTLX":1,"SJXH":"PE-CL00","EEEE":5555}其中"EEEE":5555 是从CZLY:{EEEE:5555}里面转进去的
		 */
		String js = "{INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"},\"XTLX\":1,\"SJXH\":\"PE-CL00\",ZZZZ:9999,YHKB:[{YYYY:7777},{WWWW:8888}],YHID:[\"AA\",\"BB\"],CZLY:{CCCC:333,DDDD:{FFFF:4444},EEEE:5555,GGGG:{HHHH:6666}}}";
		String maptable = "{.INFO.WKBH:.BODY.WKBH,.XTLX:.BODY.XTLX,.SJXH:.BODY.SJXH,.YHKB<>:.YHKB1<>,.CZLY.DDDD.:.CZLY2.DDDD2.,.CZLY.EEEE:.BODY.EEEE,.CZLY.GGGG.HHHH:.CZLY.GGGG.HHHH2}";

		/**
		 * 升级INFO节点中的所有k/v到一级节点
		 */
//		js = "{action:\"member/login\",\"INFO\":{\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\",\"YDZD\":\"a0000055662c93\",\"QQIP\":\"b8-bc-1b-09-52-f8\",\"JRLS\":\"201607281206414207651\",\"IMSI\":\"460037420104887\",\"YWMA\":\"1006\",\"JRDM\":\"00003\",\"JYMA\":\"0001\",\"SFYY\":\"0\",\"CZMA\":\"21\"},\"BODY\":{\"XTLX\":1,\"SJXH\":\"PE-CL00\",\"SJPP\":\"Huawei\",\"DLFS\":1,\"DLZH\":\"13918401600\",\"DLIP\":\"b8-bc-1b-09-52-f8\",\"XTBB\":\"4.4.2\",\"DLMM\":\"qwerty\",YHKB:[{YHID:1,YHBC:\"cmb\"},{YHID:2,YHBC:\"icbc\"}]}}";
//		maptable = "{.INFO.:.,.BODY.SJXH:.SJXH}";

		/**
		 * 降级k/v到INFO节点中
		 */
//		js = "{\"action\":\"member/login\",\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\",\"YDZD\":\"a0000055662c93\",\"QQIP\":\"b8-bc-1b-09-52-f8\",\"JRLS\":\"201607281206414207651\",\"IMSI\":\"460037420104887\",\"YWMA\":\"1006\",\"JRDM\":\"00003\",\"JYMA\":\"0001\",\"SFYY\":\"0\",\"CZMA\":\"21\",\"SJXH\":\"PE-CL00\",\"BODY\":{\"XTLX\":1,\"SJPP\":\"Huawei\",\"DLFS\":1,\"DLZH\":\"13918401600\",\"DLIP\":\"b8-bc-1b-09-52-f8\",\"XTBB\":\"4.4.2\",\"DLMM\":\"qwerty\",\"YHKB\":[{\"YHID\":1,\"YHBC\":\"cmb\"},{\"YHID\":2,\"YHBC\":\"icbc\"}]}}";
//		maptable = "{.CZLY:.INFO.CZLY,.WKBH:.INFO.WKBH}";
		
		/**
		 * 将二级K/V与一级K/V换到新建的BODY里面
		 */
//		js = "{INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"},\"XTLX\":1,\"SJXH\":\"PE-CL00\",ZZZZ:9999}";
//		maptable = "{.INFO.WKBH:.BODY.WKBH,.XTLX:.BODY.XTLX,.SJXH:.BODY.SJXH}";

		
	
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
			System.out.println("-----------result:"+result);
			
			/**
			 * 由于解析遍历JSON时是由前到后、由浅到深，
			 * 如要转换某二级子对象，同时又要转换该对象的子对象（孙对象），必须先转换孙对象，再转换该子对象
			 * 所以转换必须由深到浅转换，转换要分成多次完成，配置两个maptable，分两次调用转换方法。
			 */
			maptable = "{.INFO.:.}";
			map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
			System.out.println(result);
			System.out.println(maptable);
			result=tj.convertJson(result,map);
			System.out.println("-----------result:"+result);
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
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
			insertJe(nodeName, subResja,resJe);
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
								resJo.put(k, tmpArr);
							}
							if (key.substring(pos + 2, pos + 3).equals(".")) {
								key = key.substring(pos + 3, key.length());
								JSONObject tmpNode = new JSONObject();
								tmpArr.add(tmpNode);
								insertNode(key,reqJe,tmpNode);
							}
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
	 * 1.只改变key名称返回false
	 * .CZLY.DDDD:.CZLY.DDDD2 返回 false
	 * 2.改变上一级节点名称返回true
	 * .CZLY.DDDD:.CZLY2.DDDD2 返回 true
	 */
	private boolean compNodeName(String oldNodeStr,String newNodeStr){
		if(oldNodeStr.split("\\<>").length!=newNodeStr.split("\\<>").length){
			return true;
		}else if(oldNodeStr.split("\\.").length!=newNodeStr.split("\\.").length){
			return true;
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
		if(!Arrays.equals(__oldNodeArray, __newNodeArray)){
			return true;
		}else{
			return false;
		}
	}
	
	/*
	 * 往res里面添加req,节点名称为key
	 */
	private void insertJe( String nodeName,Object req,JSON res) throws Exception{
//		System.out.println(nodeName+" nodeName==insertJe==root "+root);
//		System.out.println(nodeName+" nodeName==insertJe==req "+req+"=="+res);
		if(nodeName.equals(".")){//最后
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
//			System.out.println(key);
			String keyName=getKeyName(key);
//			System.out.println(keyName);
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
//			ja.add(key, value);
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

