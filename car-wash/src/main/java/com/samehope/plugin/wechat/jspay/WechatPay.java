package com.samehope.plugin.wechat.jspay;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.samehope.common.utils.CommonUtil;
import com.samehope.common.utils.DateUtils;
import com.samehope.common.utils.NumberUtils;
import com.samehope.common.utils.RandomUtils;
import com.samehope.common.utils.StringUtils;
import com.samehope.core.render.JsonResult;
import com.samehope.plugin.wechat.utils.jspay.GetWxOrderno;
import com.samehope.plugin.wechat.utils.jspay.Sha1Util;
import com.samehope.plugin.wechat.utils.jspay.SignUtil;

//@RouterMapping(url="/wechat/pay",viewPath="/view/pay")
@Clear
public class WechatPay extends Controller{
	
	private static final Log log = Log.getLog(WechatPay.class);
	
	/**
	 * 统一下单,生成预订单
	 * @throws Exception
	 */
	public void  toJspayOuth2() throws Exception{
		String openId = getPara("openId");
		log.info("openId="+openId);
		String orderId = getPara("orderId");
		log.info("orderId="+orderId);
		String appid = getPara("appId");
		log.info("appid="+appid);
		String secret = getPara("secret");
		log.info("secret="+secret);
		String partner = getPara("partner");
		log.info("partner="+partner);
		String partnerkey = getPara("partnerkey");
		log.info("partnerkey="+partnerkey);
		String finalmoney = NumberUtils.formatAmtY2F(getPara("payMoney"));
		log.info("finalmoney="+finalmoney);
		String controllerKey = getPara("controllerKey");
		log.info("controllerKey="+controllerKey);
		String methodName = getPara("methodName");
		log.info("methodName="+methodName);
		String serverName = getPara("serverName");
		log.info("serverName="+serverName);
		
		if(!StringUtils.isBlank(openId)){
			Map<String ,String > map=new HashMap<String,String>();
			String currTime = DateUtils.getCurrTime();//8位日期
			String strTime = currTime.substring(8, currTime.length());//4位随机数
			String strRandom = RandomUtils.buildRandom(4) + "";
	        String nonceStr= strTime + strRandom;//微信要求：随机字符串，长度要求在32位以内
	        
	        map.put("appid", appid);
	        map.put("mch_id", partner);
	        map.put("nonce_str",nonceStr);
	        map.put("body",  "生成预支付订单");
	        map.put("out_trade_no", orderId);
	        map.put("total_fee", finalmoney);
	        map.put("spbill_create_ip",getRequest().getRemoteAddr());
	        String projectName = getRequest().getContextPath();//PropKit.use("wx_config.properties").get("project_name").trim();
	        String notifyUrl = "http://"+serverName + projectName+PropKit.use("wx_config.properties").get("callBackNotifyUrl").trim();
	        log.info("notify_url="+notifyUrl);
	        map.put("notify_url", notifyUrl);
	        map.put("trade_type", "JSAPI");
	        map.put("openid", openId);
	        String paySign = SignUtil.getPayCustomSign(map,partnerkey);
	        map.put("sign",paySign);
	        String xml= CommonUtil.ArrayToXml(map);
	        
	        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String prepay_id="";
			try {
				prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	        
	        log.info("prepay_id====================="+prepay_id);
	        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
			String timeStamp = Sha1Util.getTimeStamp();
			String prepay_id2 = "prepay_id="+prepay_id;
			String packages = prepay_id2;
			finalpackage.put("appId", appid);  
			finalpackage.put("timeStamp", timeStamp);  
			finalpackage.put("nonceStr", nonceStr);  
			finalpackage.put("package", packages);  
			finalpackage.put("signType", "MD5");
			String finalsign = SignUtil.getPayCustomSign(finalpackage,partnerkey);
			
			//String userInfoXml = CommonUtil.ArrayToXml(userMap);
			String payPageUrl = "pay.jsp?appid="+appid+"&timeStamp="+timeStamp+"&nonceStr="+nonceStr+"&package="+packages+
					"&sign="+finalsign;
			log.info(payPageUrl);
			setAttr("appid", appid);
			setAttr("timeStamp", timeStamp);
			setAttr("nonceStr", nonceStr);
			setAttr("package", packages);
			setAttr("sign", finalsign);
			setAttr("controllerKey", controllerKey);
			setAttr("methodName", methodName);
			setAttr("domainName", "http://"+serverName+projectName);
			renderJsp(payPageUrl);
		}else{
			//todo 跳转至授权
		}
	}
	
	/**
	 * 支付成功后，执行回调方法，对支付结果验签
	 * @param request
	 * @return
	 */
	public static Map<String,String> checkPayResult(HttpServletRequest request){
		Map<String,String> maps = new HashMap<String,String>();
		String transaction_id = "";
		String out_trade_no = "";
		String total_fee = "";
		InputStream inputStream = null;
		Document document = null;
		try {
			inputStream = request.getInputStream();
			// 读取输入流
			SAXBuilder saxbBuilder = new SAXBuilder();
			document = saxbBuilder.build(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			maps.put("rtnCode", "1");
			maps.put("rtnMsg", "读取流失败");
			return maps;
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<?> node = root.getChildren();
		for (int i = 0; i < node.size(); i++) {
			Element element = (Element) node.get(i);
			//System.out.println(element.getName() + element.getText());
			// 返回状态码
			if (element.getName().equals("return_code")) {
				String return_code =element.getText();
				if("success".equals(return_code.toLowerCase())){
					maps.put("rtnCode", "0");
					maps.put("rtnMsg", return_code.toLowerCase());
				}else{
					maps.put("rtnCode", "1");
					maps.put("rtnMsg", return_code.toLowerCase());
				}
			}
			if(element.getName().equals("total_fee")){
            	total_fee =element.getText();
            	maps.put("payMoney", total_fee);
             }
			// 微信支付订单号
			if (element.getName().equals("transaction_id")) {
				transaction_id = element.getText();
				maps.put("transactionId", transaction_id);
			}
			// 商户订单号
			if (element.getName().equals("out_trade_no")) {
				out_trade_no = element.getText();
				maps.put("orderId", out_trade_no);
			}
		}

		// 释放资源
		try {
			inputStream.close();
			inputStream = null;
		} catch (IOException e) {
			e.printStackTrace();
			maps.put("rtnCode", "1");
			maps.put("rtnMsg", "释放流对象失败");
			return maps;
		}
		return maps;
	}
	
	/**
	 * 支付成功后，执行回调方法，对支付结果判断后，向微信服务器进行反馈
	 * @param resultStr
	 * @param response
	 * @return
	 */
	public static JsonResult responWXResult(String resultStr,HttpServletResponse response){
		JsonResult json = new JsonResult();
		StringBuilder sbs = new StringBuilder();  
        sbs.append("<xml>");  
        if(!StringUtils.isBlank(resultStr)&&"success".equals(resultStr.toLowerCase())){
        	sbs.append("<return_code>SUCCESS</return_code>"); 
        	sbs.append("<return_msg>OK</return_msg>"); 
        }else{
        	sbs.append("<return_code>FAIL</return_code>"); 
        	sbs.append("<return_msg><![CDATA[支付回调处理失败]]></return_msg>"); 
        }
        sbs.append("</xml>");
        log.info("responWXResult="+sbs.toString());
        PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(sbs.toString());
		} catch (IOException e) {
			e.printStackTrace();
			json.setRtnCode(1);
			json.setRtnMsg("发送失败");
			return json;
		}
		json.setRtnCode(0);
		json.setRtnMsg("发送成功");
		json.setData("");
		return json;
	}
	
	
}
