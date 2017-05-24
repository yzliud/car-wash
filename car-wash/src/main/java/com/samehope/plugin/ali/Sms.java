package com.samehope.plugin.ali;

import java.util.List;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.jfinal.kit.PropKit;

public class Sms {
	
	public static void sendMsg(List<String> mobileList, String code) {
        /**
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount(
        		PropKit.use("ali_config.properties").get("AccessId")
        		,PropKit.use("ali_config.properties").get("AccessKey")
        		,PropKit.use("ali_config.properties").get("MNSEndpoint")
        		);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(PropKit.use("ali_config.properties").get("Topic"));
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(PropKit.use("ali_config.properties").get("SignName"));
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(PropKit.use("ali_config.properties").get("SMSTemplateCode"));
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("code", code);
        smsReceiverParams.setParam("product", PropKit.use("ali_config.properties").get("SMSTemplateCode_product"));
        // 3.4 增加接收短信的号码
        for(String mobile: mobileList){
        	batchSmsAttributes.addSmsReceiver(mobile, smsReceiverParams);
        }
        //batchSmsAttributes.addSmsReceiver("15298467600", smsReceiverParams);
        //batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber2", smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + ret.getMessageId());
            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
            System.out.println("MessageMD5: " + ret.toString());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }

}
