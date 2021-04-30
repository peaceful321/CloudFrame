package com.cloud.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.PutObjectResult;
import com.cloud.CloudObj;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OssObject {

    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args) {


        testUpload();

//        testDelete();



    }


    /**
     * 测试方法
     */
    public static void testUpload() {
        //测试简单上传；
        Upload("/Users/xuhuanchao/Documents/0.Account/DSC_2096.jpg");

        //测试多文件上传；
//        String[] urls = new String[3];
//        urls[0] = "/Users/xuhuanchao/Documents/0.Account/idcard.jpg";
//        urls[1] = "/Users/xuhuanchao/Documents/0.Account/北外异地考试证明.jpg";
//        urls[2] = "/Users/xuhuanchao/Documents/0.Account/个人简历_徐焕超_上海.pdf";
//        UploadFiles(urls);



    }

    public static void testDelete() {
        deleteFile("DSC_2096.jpg");
    }


    /**
     * 单个文件上传
     * @param uploadFileUrl 需要上传的文件URL
     * @return
     */
    protected static void Upload(String uploadFileUrl) {
        if (uploadFileUrl.isEmpty() || uploadFileUrl == null) {
            return;
        }
        try {
            File uploadFile = new File(uploadFileUrl);
            BucketObj bucketObj = new BucketObj();
            OSS ossClient = new OSSClientBuilder().build(bucketObj.getEndPoint(), CloudObj.AK, CloudObj.SK);
            PutObjectResult putObjectResult = ossClient.putObject(bucketObj.getBucketName(), uploadFile.getName(), new FileInputStream(uploadFile));
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 多文件上传
     * @param fileUrls 所有需要上传的文件urls
     * @return
     */
    protected static void UploadFiles(String... fileUrls) {
        if (fileUrls == null || fileUrls.length == 0) {
            return;
        }
        try {
            BucketObj bucketObj = new BucketObj();
            OSS ossClient = new OSSClientBuilder().build(bucketObj.getEndPoint(), CloudObj.AK, CloudObj.SK);
            for (String url : fileUrls) {
                File f = new File(url);
                ossClient.putObject(bucketObj.getBucketName(), f.getName(), f);
            }
            ossClient.shutdown();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 删除 oss 指定空间中的文件
     * @param fileName 文件名称
     */
    protected static void deleteFile(String fileName) {
        if (fileName.isEmpty() || fileName == null) {
            return;
        }
        BucketObj bucket = new BucketObj();
        try {
            OSS ossClient = new OSSClientBuilder().build(bucket.getEndPoint(), CloudObj.AK, CloudObj.SK);
            ossClient.deleteObject(bucket.getBucketName(), fileName);
            ossClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量删除的文件
     * @param fileNames 批量删除的文件名称
     */
    protected static List<String> batchDeleteFiles(List<String> fileNames) {
        if (fileNames == null || fileNames.size() == 0)
            return null;
        List<String> delObjs = new ArrayList<String>();
        try {
            BucketObj bucket = new BucketObj();
            OSS ossClient = new OSSClientBuilder().build(bucket.getEndPoint(), CloudObj.AK, CloudObj.SK);
            DeleteObjectsRequest delObjRequest = new DeleteObjectsRequest(bucket.getBucketName()).withKeys(fileNames);
            DeleteObjectsResult delObjResult = ossClient.deleteObjects(delObjRequest);
            delObjs = delObjResult.getDeletedObjects();
            ossClient.shutdown();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return delObjs;
    }





    /**
     * 内部类：空间Bucket 对象
     */
    static class BucketObj {
        private String bucketName = "xuhuanchao";
        private String endPoint = "https://oss-cn-shanghai.aliyuncs.com";
        private String domain;

        public BucketObj() {
        }

        public BucketObj(String bucketName, String endPoint, String domain, double size, int amount) {
            this.bucketName = bucketName;
            this.endPoint = endPoint;
            this.domain = domain;
        }

        public String getBucketName() {
            return bucketName;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public String getDomain() {
            return domain;
        }
    }

}
