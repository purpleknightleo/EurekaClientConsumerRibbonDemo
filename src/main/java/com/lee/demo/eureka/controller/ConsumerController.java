package com.lee.demo.eureka.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用远程服务"compute-service"，利用Ribbon做负载均衡
 *
 * Created by hzlifan on 2017/3/7.
 */
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate        restTemplate;

    // 远程服务名
    private static final String SERVICE_NAME = "compute-service";

    /**
     * 调用远程服务（GET），传入URL和返回值的类型，返回结果
     * 同时指定出错时的回调方法fallbackMethod
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "addServiceFallback")
    @RequestMapping(value = "/ribbon/add", method = RequestMethod.GET)
    public String add() {
        return restTemplate.getForEntity("http://" + SERVICE_NAME + "/add?a=8&b=24", String.class)
            .getBody();
    }

    /**
     * 调用远程服务（POST），传入URL、请求体和返回值的类型，返回结果
     * 同时指定出错时的回调方法fallbackMethod
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "addServiceFallback")
    @RequestMapping(value = "/ribbon/sub", method = RequestMethod.GET)
    public String sub() {
        Map<String, Object> params = new HashMap<String, Object>();  // POST请求体
        params.put("a", 7);
        params.put("b", 3);
        return restTemplate.postForEntity("http://" + SERVICE_NAME + "/sub", params, String.class)
            .getBody();
    }

    /**
     * 出错时的回调方法
     *
     * @return
     */
    private String addServiceFallback() {
        return "This is a self-defined error message with Hystrix by Fernando";
    }

}
