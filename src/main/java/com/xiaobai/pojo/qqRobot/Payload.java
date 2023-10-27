package com.xiaobai.pojo.qqRobot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;

/**
 * @author xiaobai
 * @date 2023/10/21-23:47
 *
 * {
 *   "op": 0,
 *   "d": {},
 *   "s": 42,
 *   "t": "GATEWAY_EVENT_NAME"
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payload {
    private Integer op;
    private String s;
    private Object d;
    private String t;

    public Payload(Integer op,Object d){
        this.op = op;
        this.d = d;
    }
}
