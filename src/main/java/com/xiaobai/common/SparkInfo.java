package com.xiaobai.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiaobai
 * @date 2023/10/24-21:40
 */
@Data
@Component
public class SparkInfo {
    @Value("${spark.hostUrl}")
    private  String hostUrl;

    @Value("${spark.apiKey}")
    private  String apiKey;

    @Value("${spark.apiSecret}")
    private  String apiSecret;

    @Value("${spark.appId}")
    private  String appId;

    @Value("${spark.domain}")
    private  String domain;



}
