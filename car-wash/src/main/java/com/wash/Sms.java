package com.wash;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Sms {
	
	private static String Url = "http://sms.1xinxi.cn/asmx/smsservice.aspx?";
	private static String Name = "xst";
	private static String Pwd = "9FA6EB4D075A98F84F896CF0D451";
	private static String Sign = "智慧园区";
	
	public static String send(String msg,String mobile) {
		
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(Url);

		String returnStr = "";
				
		// 发送
		InputStream is;
		try {
			// 向StringBuffer追加用户名
			sb.append("name="+Name);

			// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
			sb.append("&pwd="+Pwd);

			// 向StringBuffer追加手机号码
			sb.append("&mobile=" + mobile);

			// 向StringBuffer追加消息内容转URL标准码
			sb.append("&content="+URLEncoder.encode(msg,"UTF-8"));
			
			//追加发送时间，可为空，为空为及时发送
			sb.append("&stime=");
			
			//加签名
			sb.append("&sign="+URLEncoder.encode(Sign,"UTF-8"));
			
			//type为固定值pt  extno为扩展码，必须为数字 可为空
			sb.append("&type=pt&extno=");
			// 创建url对象
			//String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
			System.out.println("sb:"+sb.toString());
			URL url = new URL(sb.toString());

			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			is = url.openStream();
			
			//转换返回值
			returnStr = convertStreamToString(is);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(returnStr);
		return returnStr;
	}
	
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            try {
                is.close();
            } catch (IOException e) {    
               e.printStackTrace();    
            }    
        }    
        return sb1.toString();    
    }
	
	public static void main(String[] args){
		String msg = "您已成功在wifiBao购买套餐";
		send(msg,"18852890966");
//		send("您的验证码是：6666。请不要把验证码泄露给其他人。","18852890966");
	}
	/*0	提交成功
	1	含有敏感词汇
	2	余额不足
	3	没有号码
	4	包含sql语句
	10	账号不存在
	11	账号注销
	12	账号停用
	13	IP鉴权失败
	14	格式错误
	-1	系统异常*/
}
