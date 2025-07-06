package com.academy.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class AbstractRestClient {

    public abstract String getBaseUrl();

    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    public String getParameterizedPath() {
        return "";
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
        if (!CollectionUtils.isEmpty(headers)){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        return httpHeaders;
    }

    public String getFullUrl(Map<String, String> queryParams) {
        return getBaseUrl() + getParameterizedPath() + getQueryPath(queryParams);
    }


    public Class<?> get(Class<?> classType){
        return get(classType, null);
    }

    public Class<?> get(Class<?> classType, Map<String, String> queryParams) {
         return get(classType, queryParams, null);
    }

    public Class<?> get(Class<?> classType, Map<String, String> queryParams, Map<String, String> headers) {

        return doGenericCall(classType, null, queryParams, headers, HttpMethod.GET);
    }

    public Class<?> post(Class<?> classType, Object body){
        return post(classType, body, null);
    }

    public Class<?> post(Class<?> classType, Object body, Map<String, String> queryParams) {
        return post(classType, body, queryParams, null);
    }

    public Class<?> post(Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {
        return doGenericCall(classType, body, queryParams, headers, HttpMethod.POST);
    }

    public Class<?> put(Class<?> classType, Object body){
        return put(classType, body, null);
    }
    public Class<?> put(Class<?> classType, Object body, Map<String, String> queryParams) {
        return put(classType, body, queryParams, null);
    }

    public Class<?> put(Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {

        return doGenericCall(classType, body, queryParams, headers, HttpMethod.PUT);
    }


    public Class<?> patch(Class<?> classType, Object body){
        return patch(classType, body, null);
    }

    public Class<?> patch(Class<?> classType, Object body, Map<String, String> queryParams) {
        return patch(classType, body, queryParams, null);
    }

    public Class<?> patch(Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers) {
        return doGenericCall(classType, body, queryParams, headers, HttpMethod.PATCH);
    }



    private Class<?> doGenericCall(Class<?> classType, Object body, Map<String, String> queryParams, Map<String, String> headers, HttpMethod httpMethod) {
        String url = getFullUrl(queryParams);
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
