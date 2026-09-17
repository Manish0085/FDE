package com.stream.ai_agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WeatherTool {

    private final RestClient restClient;
    private final String apiKey;

    public WeatherTool(
            RestClient.Builder builder,
            @Value("${weather.api.key}") String apiKey
    ) {
        this.restClient = builder
                .baseUrl("https://api.weatherapi.com/v1")
                .build();

        this.apiKey = validateApiKey(apiKey);
    }

    @Tool(
            name = "get_current_weather",
            description = """
                    Gets the current weather conditions for a city.

                    Returns weather information including:
                    temperature, feels-like temperature, condition,
                    humidity, wind speed, pressure, visibility,
                    and precipitation.

                    The city can be provided as a city name such as:
                    London, Delhi, Mumbai, New York.

                    Use this tool whenever the user asks about
                    the current weather or current temperature.
                    """
    )
    public String getWeather(

            @ToolParam(
                    description = """
                            Name of the city for which current weather
                            information is required.
                            Example: Delhi, London, Mumbai.
                            """
            )
            String city
    ) {

        System.out.println("Weather tool called");

        String normalizedCity = normalizeCity(city);

        try {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", apiKey)
                            .queryParam("q", normalizedCity)
                            .build())
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException e) {

            throw new WeatherApiException(
                    "Unable to retrieve weather for city: "
                            + normalizedCity,
                    e
            );
        }
    }

    private String normalizeCity(String city) {

        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "City must not be null or blank"
            );
        }

        return city.trim();
    }

    private String validateApiKey(String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Weather API key is not configured. " +
                            "Set the 'weather.api.key' property."
            );
        }

        return apiKey.trim();
    }

    public static class WeatherApiException
            extends RuntimeException {

        public WeatherApiException(String message) {
            super(message);
        }

        public WeatherApiException(
                String message,
                Throwable cause
        ) {
            super(message, cause);
        }
    }
}