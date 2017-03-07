package com.lee.demo.eureka.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private static final String SERVICE_NAME = "compute-service".toUpperCase();

    /**
     * 调用远程服务，传入URL和返回值的类型，返回内容
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
     * 出错时的回调方法
     *
     * @return
     */
    private String addServiceFallback(){
        return "This is a self-defined error message with Hystrix by Fernando";
    }

}
