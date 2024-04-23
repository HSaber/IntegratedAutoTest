package com.autotest.api.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;

public class SendTopic {


        public static void main(String[] args) {
            CloudAccount account = new CloudAccount(
                    ServiceSettings.getMNSAccessKeyId(),
                    ServiceSettings.getMNSAccessKeySecret(),
                    ServiceSettings.getMNSAccountEndpoint());
            MNSClient client = account.getMNSClient();
            try{
                CloudQueue queue = client.getQueueRef("queue-demo");
                for (int i = 0; i < 10; i++)
                {
                    Message message = new Message();
                    message.setMessageBody("demo_message_body" + i);
                    Message putMsg = queue.putMessage(message);
                    System.out.println("Send message id is: " + putMsg.getMessageId());
                    System.out.println("{\"openid\":\"o0yEvxKIgCatMNfT838KoMmeNvxA\",\"event_category\":\"follow-unfollow\",\"event_type\":\"subscribe\",\"keyword\":\"subscribe\",\"create_time\":\"2021-09-29 10:22:38\",\"datas\":{\"eventName\":\"subscribe\",\"eventKey\":\"134784\",\"msgType\":\"event\",\"source_type\":1,\"source_value\":\"134784\",\"subscribeEventName\":\"qrcode_scan\",\"qrcodeId\":\"134784\",\"qrcodeName\":\"Fission_gytest0928_4134\",\"qrcodeCategoryId\":\"2956\",\"source_medium\":\"2956\",\"qrcodeCategoryName\":\"Fission_gytest0928(5092)\",\"is_first_subscribe\":0},\"mid\":\"807\",\"user_source_id\":0}");
                }
            } catch (ClientException ce)
            {
                System.out.println("Something wrong with the network connection between client and MNS service."
                        + "Please check your network and DNS availablity.");
                ce.printStackTrace();
            } catch (ServiceException se)
            {
                if (se.getErrorCode().equals("QueueNotExist"))
                {
                    System.out.println("Queue is not exist.Please create before use");
                } else if (se.getErrorCode().equals("TimeExpired"))
                {
                    System.out.println("The request is time expired. Please check your local machine timeclock");
                }
                se.printStackTrace();
            } catch (Exception e)
            {
                System.out.println("Unknown exception happened!");
                e.printStackTrace();
            }

            client.close();
        }


}
