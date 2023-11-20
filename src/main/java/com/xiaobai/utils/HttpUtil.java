package com.xiaobai.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaobai
 * @date 2023/10/23-20:59
 */
@Slf4j
public class HttpUtil {
    public static Map<String, Object> executeRequest(String url, HttpMethod method, JSONObject params, Header... headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            //创建请求
            HttpRequestBase request = null;
            switch (method) {
                case GET:
                    request = new HttpGet(url);
                    if (null != headers && headers.length > 0) {
                        request.setHeaders(headers);
                    }
                    break;
                case POST:
                    request = new HttpPost(url);


                    //提交参数发送请求
                    if (Objects.nonNull(headers)) {
                        request.setHeaders(headers);
                    }
                    request.setHeader("Content-Type", "application/json");
                    StringEntity param = new StringEntity(params.toString(), "UTF-8");
                    ((HttpPost) request).setEntity(param);
            }
            log.info("{}",request);
            response = httpClient.execute(request);
            //5.得到响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            //6. 判断响应信息是否正确
            if (HttpStatus.SC_OK != statusCode) {
                request.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            //7. 转换成实体类
            HttpEntity entity = response.getEntity();

            if (null != entity) {
                return JSONObject.parseObject(EntityUtils.toString(entity, "UTF-8"));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //8. 关闭所有资源连接
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static Map<String, Object> executeRequest(String url, HttpMethod method) {
        return executeRequest(url, method, null);
    }
}
