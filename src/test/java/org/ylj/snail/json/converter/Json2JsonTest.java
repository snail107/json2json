package org.ylj.snail.json.converter;

import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.ylj.snail.json.util.ToolUtil;

public class Json2JsonTest {

	private String js ="";
	private String maptable ="";
	/**
	 * 升级INFO节点中的所有k/v到一级节点
	 */
	@Test
	public void testConvertJson1() {
		System.out.println("---------------------testConvertJson1----------------------");
		Json2Json tj = new Json2Json();
		js = "{action:\"member/login\",\"INFO\":{\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\",\"YDZD\":\"a0000055662c93\"},\"BODY\":{\"XTLX\":1,\"SJXH\":\"PE-CL00\"}}";
		maptable = "{.INFO.:.,.BODY.SJXH:.SJXH}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);
			Assert.assertEquals(result.toString(), "{\"action\":\"member/login\",\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\",\"YDZD\":\"a0000055662c93\",\"SJXH\":\"PE-CL00\",\"BODY\":{\"XTLX\":1}}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 降级k/v到INFO节点中
	 */
	@Test
	public void testConvertJson2() {
		System.out.println("---------------------testConvertJson2----------------------");
		Json2Json tj = new Json2Json();
		js = "{\"action\":\"member/login\",\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"}";
		maptable = "{.CZLY:.INFO.CZLY,.WKBH:.INFO.WKBH}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);
			Assert.assertEquals(result.toString(), "{\"action\":\"member/login\",\"INFO\":{\"CZLY\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"}}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 将二级K/V与一级K/V换到新建的BODY里面
	 */
	@Test
	public void testConvertJson3() {
		System.out.println("---------------------testConvertJson3----------------------");
		Json2Json tj = new Json2Json();
		js = "{INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"},\"XTLX\":1,\"SJXH\":\"PE-CL00\",ZZZZ:9999}";
		maptable = "{.INFO.WKBH:.BODY.WKBH,.XTLX:.BODY.XTLX,.SJXH:.BODY.SJXH}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);
			Assert.assertEquals(result.toString(), "{\"BODY\":{\"WKBH\":\"b8:bc:1b:09:52:f8\",\"XTLX\":1,\"SJXH\":\"PE-CL00\"},\"INFO\":{\"GGGG\":\"02\"},\"ZZZZ\":9999}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 解决以下情况的转换
	 * {"BODY":{YHKB:[{YHID:1,YHBC:"cmb"},{YHID:2,YHBC:"icbc"}]}} 
	 * {.BODY.YHKB<>.YHBC:.blist<>.bankName,.BODY.YHKB<>.YHID:.blist<>.bankID}
	 * subAC_tag=true 标识Array里面的objcet存在转换
	 * subAC_nodeName 记录Array的KEY名称
	 * 解析到<>.YHID时 subAC_tag标识为true,并记录subAC_nodeName
	 */
	@Test
	public void testConvertJson4() {
		System.out.println("---------------------testConvertJson4----------------------");
		Json2Json tj = new Json2Json();
		js = "{\"BODY\":{\"XTLX\":1,YHKB:[{YHID:1,YHBC:\"cmb\"},{YHID:2,YHBC:\"icbc\"}]},\"action\":\"member/login\",\"INFO\":{\"CZLY\":\"02\"}}";
		maptable = "{.INFO.CZLY:.channel,.BODY.XTLX:.XTLX,.BODY.YHKB<>.YHBC:.blist<>.bankName,.BODY.YHKB<>.YHID:.blist<>.bankID}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);
			Assert.assertEquals(result.toString(), "{\"XTLX\":1,\"blist\":[{\"bankID\":1,\"bankName\":\"cmb\"},{\"bankID\":2,\"bankName\":\"icbc\"}],\"action\":\"member/login\",\"channel\":\"02\"}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}

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
	@Test
	public void testConvertJson5() {
		System.out.println("---------------------testConvertJson5----------------------");
		Json2Json tj = new Json2Json();
		js = "{INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\"},\"XTLX\":1,\"SJXH\":\"PE-CL00\",ZZZZ:9999,YHKB:[{YYYY:7777},{WWWW:8888}],YHID:[\"AA\",\"BB\"],CZLY:{CCCC:333,DDDD:{FFFF:4444},EEEE:5555,GGGG:{HHHH:6666}}}";
		maptable = "{.INFO.WKBH:.BODY.WKBH,.XTLX:.BODY.XTLX,.SJXH:.BODY.SJXH,.YHKB<>:.YHKB1<>,.CZLY.DDDD.:.CZLY2.DDDD2.,.CZLY.EEEE:.BODY.EEEE,.CZLY.GGGG.HHHH:.CZLY.GGGG.HHHH2}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);
			Assert.assertEquals(result.toString(),"{\"BODY\":{\"WKBH\":\"b8:bc:1b:09:52:f8\",\"XTLX\":1,\"SJXH\":\"PE-CL00\",\"EEEE\":5555},\"INFO\":{\"GGGG\":\"02\"},\"ZZZZ\":9999,\"YHKB1\":[{\"YYYY\":7777},{\"WWWW\":8888}],\"YHID\":[\"AA\",\"BB\"],\"CZLY2\":{\"DDDD2\":{\"FFFF\":4444}},\"CZLY\":{\"CCCC\":333,\"GGGG\":{\"HHHH2\":6666}}}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * 由于解析遍历JSON时是由前到后、由浅到深，
	 * 如要转换某二级子对象，同时又要转换该对象的子对象（孙对象），必须先转换孙对象，再转换该子对象
	 * 所以转换必须由深到浅转换，转换要分成多次完成，配置两个maptable，分两次调用转换方法。
	 */
	@Test
	public void testConvertJson6() {
		System.out.println("---------------------testConvertJson6----------------------");
		Json2Json tj = new Json2Json();
		js = "{INFO:{\"GGGG\":\"02\",\"WKBH\":\"b8:bc:1b:09:52:f8\",\"XTLX\":1}}";
		maptable = "{.INFO.WKBH:.BODY.WKBH}";
		Map<String, String> map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
		try {
			System.out.println(js);
			System.out.println(maptable);
			JSONObject result=tj.convertJson(JSONObject.fromObject(js),map);
//			System.out.println("-----------result:"+result);

			/**
			 * 再次转换
			 */
			maptable = "{.INFO.:.}";
			map=ToolUtil.json2Map(JSONObject.fromObject(maptable));
			System.out.println(result);
			System.out.println(maptable);
			result=tj.convertJson(result,map);
//			System.out.println("-----------result:"+result);
			
			Assert.assertEquals(result.toString(), "{\"BODY\":{\"WKBH\":\"b8:bc:1b:09:52:f8\"},\"GGGG\":\"02\",\"XTLX\":1}");
		} catch (Exception e) {
			System.out.println("------"+e.getMessage());
			e.printStackTrace();
		}
	}
}