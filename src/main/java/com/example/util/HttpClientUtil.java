package com.example.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

  public  final  static  String  errorCode = "-99999";


    public static String get(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String ret = null;
        Map errRet =  new HashMap<>();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
           // httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                   // System.out.println("Response content: " + EntityUtils.toString(entity));
                    ret = EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errRet.put("code",errorCode);
            errRet.put("msg",e.getMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
                errRet.put("code",errorCode);
                errRet.put("msg",e.getMessage());
            }
        }
        if(ret == null && errRet!=null && errRet.size()>0){
            ret = JSON.toJSONString(errRet);
        }
        return  ret;
    }


    public static String postForm(String url,Map<String,String> dataParam) {
        String ret = null;
        Map errRet =  new HashMap<>();
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        // httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
        // 创建参数队列
        UrlEncodedFormEntity uefEntity;
        try {
            List formparams = new ArrayList();
            for(String key: dataParam.keySet()){
                formparams.add(new BasicNameValuePair(key, dataParam.get(key)));
            }
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            //httpPost.setHeader("charset", "UTF-8");
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    ret =  EntityUtils.toString(entity, "UTF-8") ;
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            errRet.put("code",errorCode);
            errRet.put("msg",e.getMessage());
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
                errRet.put("code",errorCode);
                errRet.put("msg",e.getMessage());
            }
        }
        if(ret == null && errRet!=null && errRet.size()>0){
            ret = JSON.toJSONString(errRet);
        }
        return  ret;
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        URLConnection conn = null;
        StringBuilder jsonStr = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = realUrl.openConnection();
            conn.setConnectTimeout(20000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应

            InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            char[] buff = new char[1024];
            int length = 0;
            while ((length = reader.read(buff)) != -1) {
                String result = new String(buff, 0, length);
                jsonStr.append(result);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return jsonStr.toString();
    }




    /**
     * 模拟HttpPost请求
     * @param url
     * @param jsonString
     * @return
     */
    public static String HttpPost(String url, String jsonString) {
        Map errRet =  new HashMap<>();
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();//创建CloseableHttpClient
        HttpPost httpPost = new HttpPost(url);//实现HttpPost
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig); //设置httpPost的状态参数
        httpPost.addHeader("Content-Type", "application/json");//设置httpPost的请求头中的MIME类型为json
        StringEntity requestEntity = new StringEntity(jsonString, "utf-8");
        httpPost.setEntity(requestEntity);//设置请求体
        try {
            response = httpClient.execute(httpPost, new BasicHttpContext());//执行请求返回结果
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                errRet.put("code",errorCode);
                errRet.put("msg","网络请求失败");
                return JSON.toJSONString(errRet);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                return resultStr;
            } else {
                errRet.put("code",errorCode);
                errRet.put("msg","网络请求失败");
                return JSON.toJSONString(errRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errRet.put("code",errorCode);
            errRet.put("msg",e.getMessage());
            return JSON.toJSONString(errRet);
        } finally {
            if (response != null) {
                try {
                    response.close();//最后关闭response
                } catch (IOException e) {
                    e.printStackTrace();
                    errRet.put("code",errorCode);
                    errRet.put("msg",e.getMessage());
                    return JSON.toJSONString(errRet);
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    errRet.put("code",errorCode);
                    errRet.put("msg",e.getMessage());
                    return JSON.toJSONString(errRet);
                }
            }
        }
    }



    public static String postByBody(String targetUrl, String params) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000 * 10)
                .setConnectTimeout(5000 * 4)
                .setConnectionRequestTimeout(5000 * 4)
                .build();

        String result = "";
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(targetUrl);
        httpPost.setConfig(defaultRequestConfig);
        // 设置ContentType
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            //将请求参数添加到方法中
            StringEntity entity = new StringEntity(params, Consts.UTF_8);
            httpPost.setEntity(entity);

            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity,"UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
