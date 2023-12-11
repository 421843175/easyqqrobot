package com.xiaobai.utils;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.BaseVar;
import com.xiaobai.ws.QQWebSocketClient;
import com.xiaobai.ws.SparkWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.util.Strings;
import org.java_websocket.client.WebSocketClient;
import org.springframework.http.HttpMethod;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xiaobai
 * @date 2023/10/21-23:09
 */
@Slf4j
public class AuthorizationUtil {

    /**
     * 获取接口凭证
     * 请求参数：
     * {
     * "appId": "APPID",
     * "clientSecret": "CLIENTSECRET"
     * }
     */
    public static void getToken(JSONObject param) {
        BaseVar.token = (String) Objects.requireNonNull(HttpUtil.executeRequest(
                        BaseVar.TOKEN_URL,
                        HttpMethod.POST,
                        param))
                .get("access_token");
    }

    /**
     * GET /gateway
     * <p>
     * 'headers': {
     * 'Authorization': "QQBot {ACCESS_TOKEN}",
     * 'X-Union-Appid': "{BOT_APPID}",
     * }
     */
    public static void getQQWSClient(String botId) throws URISyntaxException {
        String url = BaseVar.BASE_URL + BaseVar.GATEWAY_URL;
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Authorization", "QQBot " + BaseVar.token);
        headers[1] = new BasicHeader("X-Union-Appid", botId);
        url = (String) HttpUtil.executeRequest(url, HttpMethod.GET, null, headers).get("url");

        if(Strings.isBlank(url)){
            log.info("WS连接路径为空");
            return;
        }
        BaseVar.WS_URL = url;
        //获取连接
        WebSocketClient client = new QQWebSocketClient(new URI(url));
        client.connect();   //进行wbesocket连接

    }

    public static WebSocketClient getSparkWSClient(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);


        // 拼接地址
        String httpUrl = "https://" + url.getHost() + url.getPath()
                        +
                        "?authorization="+Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))
                        +
                        "&date=" + URLEncoder.encode(date,"UTF-8")
                        +
                        "&host=" + url.getHost();

        httpUrl = httpUrl.replace("http://", "ws://").replace("https://", "wss://");

        return new SparkWebSocketClient(new URI(httpUrl));

    }

}
