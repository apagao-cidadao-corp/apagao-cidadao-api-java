
package com.apagao.cidadao.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ViaCepService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> consultar(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, Map.class);
    }
}
