package com.autotest.common.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class OSSUtil {
    private static Logger Log = Logger.getLogger(OSSUtil.class);

    // The local file path to upload.
    // OSS domain, such as http://oss-cn-hangzhou.aliyuncs.com
    public static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    // Access key Id. Please get it from https://ak-console.aliyun.com
    public static String accessKeyId = "LTAI5tK9TDbpFmHLs1aHubpC";
    public static String accessKeySecret = "ZGGEgN5pgARsBjl4oMBuEg0iLVCenC";
    // The existing bucket name
    public static String bucketName = "jingdigital-autotest";

    public static  void createFolderSample(OSS client,String foldername)throws Exception{
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        //         OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            /*
             * Create an empty folder without request body, note that the key must be
             * suffixed with a slash
             */
            final String keySuffixWithSlash = foldername+"/";
            client.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            System.out.println("Creating an empty folder " + keySuffixWithSlash + "\n");

            /*
             * Verify whether the size of the empty folder is zero
             */
            OSSObject object = client.getObject(bucketName, keySuffixWithSlash);
            System.out.println("Size of the empty folder '" + object.getKey() + "' is " +
                    object.getObjectMetadata().getContentLength());
            object.getObjectContent().close();

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        }
    }

    public static void uploadFile(OSS ossClient,String foldername,String fileKey,File filepath)throws Exception{
        try {
            if (ossClient.doesBucketExist(bucketName)) {
                System.out.println("您已经创建Bucket：" + bucketName + "。");
            } else {
                System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
                ossClient.createBucket(bucketName);
            }
            BucketInfo info = ossClient.getBucketInfo(bucketName);
            System.out.println("Bucket " + bucketName + "的信息如下：");
            System.out.println("\t数据中心：" + info.getBucket().getLocation());
            System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
            System.out.println("\t用户标志：" + info.getBucket().getOwner());

            createFolderSample(ossClient,foldername);
            ossClient.putObject(bucketName, foldername+"/"+fileKey, filepath);
            System.out.println("Object：" + fileKey + "存入OSS成功。");
            //生成报告地址
            System.out.println("获取OSSurl:"+getUrl(ossClient,foldername+"/"+fileKey));
            ObjectListing objectListing = ossClient.listObjects(bucketName);
            List<OSSObjectSummary> objectSummary = objectListing.getObjectSummaries();
           /* System.out.println("您有以下Object：");
            for (OSSObjectSummary object : objectSummary) {
                System.out.println("\t" + object.getKey());
            }*/
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        Log.info("Completed");
    }

    public static OSS connOSS(){
        Log.info("Started");
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        System.out.println(ossClient.getBucketInfo(bucketName).getBucket().getCreationDate());
        return ossClient;
    }
    public static String getUrl(OSS client,String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = client.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {

            return url.toString().replaceAll("https","http");
        }
        return null;
    }
    public static void main(String[] args) throws Exception {

//        OSSUtil ossUtil=new OSSUtil();
        File  filepath= new File("");
        OSSUtil.uploadFile(OSSUtil.connOSS(),"dev-测试报告","TestEDM2021-12-22-19-43.html",new File(filepath.getCanonicalPath()+"/test-output/TestEDM2021-12-22-19-43.html"));

    }
}
