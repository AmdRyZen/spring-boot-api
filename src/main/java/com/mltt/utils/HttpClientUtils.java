package com.mltt.utils;


import com.mltt.exception.ServiceException;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class HttpClientUtils {

    /**
     * http post
     * */
    public static String post(String url, String params) throws ServiceException {
        return  httpClient(url, HttpMethod.POST, params);
    }

    /**
     * http get
     * */
    public static String get(String url, String params) throws ServiceException {
        return  httpClient(url, HttpMethod.GET, params);
    }

    /**
     * HttpMethod  post/get
     * */
    private static String httpClient(String url, HttpMethod method, String params) throws ServiceException {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10*1000);
        requestFactory.setReadTimeout(10*1000);
        RestTemplate client = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(params, headers);
        System.out.println("requestEntity.getBody().toString() = " + Objects.requireNonNull(requestEntity.getBody()));
        //  执行HTTP请求
        ResponseEntity<String> response;
        try{
            response = client.exchange(url, method, requestEntity, String.class);
            return response.getBody();
        }
        catch (HttpClientErrorException e){
            System.out.println( "------------- 出现异常 HttpClientErrorException -------------");
            System.out.println(e.getMessage());
            System.out.println(e.getStatusText());
            System.out.println( "-------------responseBody-------------");
            System.out.println( e.getResponseBodyAsString());
            e.printStackTrace();
            return "";
        }
        catch (Exception e) {
            System.out.println( "------------- HttpClientUtils.httpRestClient() 出现异常 Exception -------------");
            System.out.println(e.getMessage());
            return "";
        }
    }
}
