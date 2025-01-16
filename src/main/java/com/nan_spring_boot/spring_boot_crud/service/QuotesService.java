package com.nan_spring_boot.spring_boot_crud.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuotesService {

    @Cacheable(value = "quotes")
    public String getDailyRandomQuotes() {

        String url = "https://zenquotes.io/api/random";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        JSONArray jsonArray = new JSONArray(response);
        JSONObject json = jsonArray.getJSONObject(0);
        String content = json.getString("q");
        return content;
    }
}
