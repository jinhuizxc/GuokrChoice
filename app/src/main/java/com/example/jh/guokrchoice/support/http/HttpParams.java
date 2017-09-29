package com.example.jh.guokrchoice.support.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dazz on 2016/4/3.
 */
public class HttpParams {
    private Map<String, String> paramMap = new HashMap<>();

    public void put(String key, String value) {
        paramMap.put(key, value);
    }

    public String getParams(){
        if (paramMap.isEmpty()) return "";
        StringBuilder params = new StringBuilder();
        params.append('?');

        for (Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator(); iterator.hasNext(); params.append("&")) {
            Map.Entry<String, String> item = iterator.next();
            String tmp = item.getKey()+"="+item.getValue();
            params.append(tmp);
        }
        return params.toString();
    }

    public static HttpParams getDefaultParams(){
        HttpParams params = new HttpParams();
        params.put("retrieve_type","by_since");
        params.put("category","all");
        params.put("limit", "20");
        params.put("ad","1");
        return params;
    }

    public static HttpParams getUpdateParams(int flag){
        HttpParams params = getDefaultParams();
        params.put("orientation","before");
        params.put("since",String.valueOf(flag));
        return params;
    }
}
