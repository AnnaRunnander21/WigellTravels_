package com.example.wigelltravels_.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PriceCalculatorClient {

    private final RestTemplate pricingRestTemplate;
    private final String baseUrl;

    public PriceCalculatorClient(RestTemplate pricingRestTemplate,
                                 @Value("${pricing.base-url}") String baseUrl) {
        this.pricingRestTemplate = pricingRestTemplate;
        this.baseUrl = baseUrl;
    }

    public double convertSekToEur(int sek) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/pricecalculator/v1/convert")
                .queryParam("sek", sek)
                .toUriString();

        Double eur = pricingRestTemplate.getForObject(url, Double.class);
        if (eur == null) {
            throw new IllegalStateException("Null response from PriceCalculator");
        }
        return eur;
    }
}
