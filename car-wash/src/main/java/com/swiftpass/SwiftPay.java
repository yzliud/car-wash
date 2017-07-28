package com.swiftpass;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.samehope.common.utils.JsonUtil;
import com.samehope.common.utils.MD5;
import com.samehope.common.utils.NumberUtils;
import com.samehope.common.utils.SignUtils;
import com.samehope.common.utils.XmlUtils;

public class SwiftPay {
	
	public static void pay(Map<String,String> map, HttpServletRequest req){
		CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        
        SortedMap<String,String> inMap = new TreeMap<String,String>();
        inMap.put("mch_id", SwiftpassConfig.mch_id);
        inMap.put("notify_url",  map.get("domain_url") + SwiftpassConfig.notify_url);
        inMap.put("callback_url",  map.get("domain_url") + SwiftpassConfig.notify_url);
        inMap.put("is_raw", SwiftpassConfig.is_raw);
        inMap.put("nonce_str", String.valueOf(new Date().getTime()));
        inMap.put("service", SwiftpassConfig.service);
        inMap.put("version", SwiftpassConfig.version);
        inMap.put("charset", SwiftpassConfig.charset);
        inMap.put("sign_type", SwiftpassConfig.sign_type);
        inMap.put("out_trade_no", map.get("orderId"));
        inMap.put("total_fee", NumberUtils.formatAmtY2F(map.get("payMoney")));
        inMap.put("body", SwiftpassConfig.body);
        inMap.put("sub_openid", map.get("sub_openid"));
        inMap.put("sub_appid", SwiftpassConfig.sub_appid);
        inMap.put("mch_create_ip", SwiftpassConfig.mch_create_ip);
        
        Map<String,String> params = SignUtils.paraFilter(inMap);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + SwiftpassConfig.key, "utf-8");
        inMap.put("sign", sign);
        
        try {
			HttpPost httpPost = new HttpPost(SwiftpassConfig.req_url);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(inMap),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			
			Map<String,String> resultMap = null;
			if(response != null && response.getEntity() != null){
			    resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
			    res = XmlUtils.toXml(resultMap);
			    System.out.println("请求结果：" + res);
			    
			    if(resultMap.containsKey("sign")){
			        if(!SignUtils.checkParam(resultMap, SwiftpassConfig.key)){
			            res = "验证签名不通过";
			        }else{
			            if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){ 
			            	
			                String pay_info = resultMap.get("pay_info");
			                Map<String,String> payInfo = JsonUtil.jsonToMap(pay_info);
			                String appId = (String) payInfo.get("appId");
			                String timeStamp = (String) payInfo.get("timeStamp");
			                String nonceStr = (String) payInfo.get("nonceStr");
			                String package1 = (String) payInfo.get("package");
			                String signType = (String) payInfo.get("signType");
			                String paySign = (String) payInfo.get("paySign");
			                req.setAttribute("timeStamp",timeStamp);
			                req.setAttribute("nonceStr",nonceStr);
			                req.setAttribute("package1",package1);
			                req.setAttribute("signType",signType);
			                req.setAttribute("paySign",paySign);
			                req.setAttribute("appId",appId);
			                //System.out.println("code_img_url"+code_img_url);
			                //req.setAttribute("code_img_url", code_img_url);
//			                req.setAttribute("out_trade_no", map.get("out_trade_no"));
//			                req.setAttribute("total_fee", map.get("total_fee"));
//			                req.setAttribute("body", map.get("body"));
			            }
			        }
			    } 
			}else{
			    res = "操作失败";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if(response != null){
	            try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        if(client != null){
	            try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	}

}
