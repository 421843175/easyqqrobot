package com.xiaobai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaobai.common.RobotInfo;
import com.xiaobai.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;



@Service
public class ToolsService {

    @Autowired
    RobotInfo robotInfo;

    @RequestMapping
    public String toolsinfo(MessageDto messageDto) {
        switch (messageDto.getContent()){
            case "随机一言":
                return sendHTTP(HttpMethod.GET,"https://tenapi.cn/v2/yiyan");
            case "历史上的今天":
                StringBuffer endstr=new StringBuffer();
                String str = sendHTTP(HttpMethod.GET,"https://tenapi.cn/v2/history");
                JSONObject jsonObject = JSON.parseObject(str);
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
             for(int i=0;i<3;i++){
                 JSONObject jo = jsonArray.getJSONObject(i);
                 String year = jo.getString("year");
                 String title = jo.getString("title");
                 endstr.append(year+"年,"+title+"\n");
             }
             return endstr.toString();
            case "百度热搜":
             endstr=new StringBuffer();
             str = sendHTTP(HttpMethod.GET,"https://tenapi.cn/v2/baiduhot");
             jsonObject = JSON.parseObject(str);
             jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<10;i++){
                JSONObject jo = jsonArray.getJSONObject(i);
                String title = jo.getString("name");
                endstr.append("TOP"+(i+1)+":"+title+"\n");
            }
            return endstr.toString();




        }
        return null;
    }

    /*
      @Author: jupiter
    * @Description: 发送http请求
    * @Param: [httpmethod, url]
    * */
    public String sendHTTP(HttpMethod httpmethod, String url){
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity  = null;
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);


        responseEntity = restTemplate.exchange(url, httpmethod,entity,String.class);


       String response=responseEntity.getBody();

        return response;
    }

}
