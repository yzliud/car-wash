<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String appId = (String)request.getAttribute("appid");
String timeStamp = (String)request.getAttribute("timeStamp");
String nonceStr = (String)request.getAttribute("nonceStr");
String packageValue = (String)request.getAttribute("package");
String paySign = (String)request.getAttribute("sign");

String domainName = (String)request.getAttribute("tempContextUrl");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>微信支付</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  </body>
  <script  src="<%=basePath%>/view/pay/js/jquery.min.js"></script>
  <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
  <script type="text/javascript">
  
  function callPay(){
	  
	  setTimeout(function(){
		  WeixinJSBridge.invoke('getBrandWCPayRequest',{
		  		 "appId" : "<%=appId%>","timeStamp" : "<%=timeStamp%>", "nonceStr" : "<%=nonceStr%>", "package" : "<%=packageValue%>","signType" : "MD5", "paySign" : "<%=paySign%>" 
		   			},function(res){
						WeixinJSBridge.log(res.err_msg);
			            if(res.err_msg == "get_brand_wcpay_request:ok"){  
			            	alert("微信支付成功!"); 
			            	window.location.href = $("#domainName").html()+"car/order/forward";
			            }else if(res.err_msg == "get_brand_wcpay_request:cancel"){  
			            	alert("用户取消支付!"); 
			            }else{  
			               alert("支付失败!");  
			            }  
					})
	  },300)
	  
			return false;
  }
  
   window.onload=function(){
	  setTimeout(function(){
		  callPay();
	  },1000)
	  
  } 
    	    	 
		
  </script>
  
</html>
