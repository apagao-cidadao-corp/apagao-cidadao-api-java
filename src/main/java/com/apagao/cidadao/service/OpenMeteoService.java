
package com.apagao.cidadao.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class OpenMeteoService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeatherData(double lat, double lon) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                     "&longitude=" + lon + "&current_weather=true";

        return restTemplate.getForObject(url, Map.class);
    }
}
