package com.academy.common.client;

import com.academy.common.constant.CommonConstant;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractRestClient {

    public abstract String getBaseUrl();

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public String getQueryPath(Map<String, String> queryParams) {
        if (!CollectionUtils.isEmpty(queryParams)) {
            StringBuilder query = new StringBuilder("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            // Remove the last '&'
            query.setLength(query.length() - 1);
            return query.toString();
        }
        return "";
    }

    public HttpHeaders getHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // Add default headers if needed
        httpHeaders.add(CommonConstant.CONTENT_TYPE, CommonConstant.APPLICATION_JSON);
        httpHeaders.add(CommonConstant.ACCEPT, CommonConstant.APPLICATION_JSON);
        // Add custom headers if provided
        if (!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        String trackingId = MDC.get(CommonConstant.X_TRACKING_ID);
        if(StringUtils.isBlank(trackingId)){
            trackingId = UUID.randomUUID().toString();
        }
        httpHeaders.add(CommonConstant.X_TRACKING_ID, trackingId);
        return httpHeaders;
    }


    public Class<?> get(String uri, Class<?> classType){
        return get(uri, classType, null);
    }

    public Class<?> get(String uri, Class<?> classType, Map<String, String> queryParams) {
         return get(uri, classType, queryParams, null);
    }

    public Class<?> get(String uri, Class<?> classType, Map<String, String> queryParams, Map<String, String> headers) {

        return doGenericCall(uri, classType, null, queryParams, headers, HttpMethod.GET);
    }

    public Class<?> post(String uri, Class<?> classType, Object body){
        return post(uri, classType, body, null);
    }

    public Class<?> post(String uri, Class<?> classType, Object body, Map<String, String> queryParams) {
        return post(uri, classType, body, queryParams, null);
    }

    public Class<?> post(String uri, Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {
        return doGenericCall(uri, classType, body, queryParams, headers, HttpMethod.POST);
    }

    public Class<?> put(String uri, Class<?> classType, Object body){
        return put(uri, classType, body, null);
    }
    public Class<?> put(String uri, Class<?> classType, Object body, Map<String, String> queryParams) {
        return put(uri, classType, body, queryParams, null);
    }

    public Class<?> put(String uri, Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {
        return doGenericCall(uri, classType, body, queryParams, headers, HttpMethod.PUT);
    }


    public Class<?> patch(String uri, Class<?> classType, Object body){
        return patch(uri, classType, body, null);
    }

    public Class<?> patch(String uri, Class<?> classType, Object body, Map<String, String> queryParams) {
        return patch(uri, classType, body, queryParams, null);
    }

    public Class<?> patch(String uri, Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {
        return doGenericCall(uri, classType, body, queryParams, headers, HttpMethod.PATCH);
    }



    private Class<?> doGenericCall(String uri, Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers, HttpMethod httpMethod) {
        String url = this.getBaseUrl() + uri + this.getQueryPath(queryParams);
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        if (classType == null) {
            throw new IllegalArgumentException("Class type cannot be null");
        }

        HttpHeaders httpHeaders = getHeaders(headers);
        HttpEntity<Object> requestEntity;
        // If body is null or empty, create an HttpEntity without a body
        if(ObjectUtils.isEmpty(body)){
            requestEntity = new HttpEntity<>(httpHeaders);
        }else {
            requestEntity = new HttpEntity<>(body, httpHeaders);
        }
        // If httpMethod is null, default to GET
        if (httpMethod == null) {
            httpMethod = HttpMethod.GET;
        }
        // Perform the REST call using RestTemplate
        try {
            return getRestTemplate().exchange(url, httpMethod, requestEntity, classType.getClass()).getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
