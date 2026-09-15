package com.agent.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Locale;
import java.util.Set;

@Component
public class CurrencyTool {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of(
            "AUD", "BGN", "BRL", "CAD", "CHF",
            "CNY", "CZK", "DKK", "EUR", "GBP",
            "HKD", "HUF", "IDR", "ILS", "INR",
            "ISK", "JPY", "KRW", "MXN", "MYR",
            "NOK", "NZD", "PHP", "PLN", "RON",
            "SEK", "SGD", "THB", "TRY", "USD", "ZAR"
    );

    private final RestClient restClient;

    public CurrencyTool(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.frankfurter.dev")
                .build();
    }

    @Tool(
            name = "currency_exchange",
            description = """
                    Gets the current/latest exchange rate between two currencies.

                    Provide ISO 4217 three-letter currency codes such as:
                    USD, EUR, GBP, INR, JPY.

                    Example:
                    USD to INR
                    EUR to USD
                    GBP to INR

                    This tool returns the exchange rate from the source
                    currency to the target currency.

                    Use this tool whenever an exact exchange rate is required.
                    """
    )
    public String getExchange(

            @ToolParam(
                    description = """
                            Source currency.
                            Must be a three-letter ISO 4217 currency code,
                            for example USD, EUR, GBP, or INR.
                            """
            )
            String from,

            @ToolParam(
                    description = """
                            Target currency.
                            Must be a three-letter ISO 4217 currency code,
                            for example USD, EUR, GBP, or INR.
                            """
            )
            String to
    ) {

        System.out.println("Currency Tool called");
        String sourceCurrency = normalizeCurrency(from);
        String targetCurrency = normalizeCurrency(to);

        validateCurrency(sourceCurrency, "from");
        validateCurrency(targetCurrency, "to");

        if (sourceCurrency.equals(targetCurrency)) {
            return String.format(
                    "1 %s = 1 %s",
                    sourceCurrency,
                    targetCurrency
            );
        }

        try {

            ExchangeRateResponse response = restClient.get()
                    .uri("/v2/rate/{from}/{to}",
                            sourceCurrency,
                            targetCurrency)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            (request, response1) -> {
                                throw new CurrencyApiException(
                                        "Frankfurter API returned HTTP "
                                                + response1.getStatusCode()
                                );
                            }
                    )
                    .body(ExchangeRateResponse.class);

            if (response == null || response.rate() == null) {
                throw new CurrencyApiException(
                        "Invalid response received from currency API"
                );
            }

            return String.format(
                    "1 %s = %s %s",
                    response.base(),
                    response.rate(),
                    response.quote()
            );

        } catch (RestClientException e) {

            throw new CurrencyApiException(
                    "Unable to retrieve exchange rate for "
                            + sourceCurrency
                            + " to "
                            + targetCurrency,
                    e
            );
        }
    }

    private String normalizeCurrency(String currency) {

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException(
                    "Currency code must not be null or blank"
            );
        }

        return currency
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private void validateCurrency(
            String currency,
            String parameterName
    ) {

        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    parameterName
                            + " must be a valid three-letter ISO 4217 "
                            + "currency code"
            );
        }

        if (!SUPPORTED_CURRENCIES.contains(currency)) {
            throw new IllegalArgumentException(
                    "Unsupported currency: " + currency
            );
        }
    }

    private record ExchangeRateResponse(
            String base,
            String quote,
            Double rate,
            String date
    ) {
    }

    public static class CurrencyApiException
            extends RuntimeException {

        public CurrencyApiException(String message) {
            super(message);
        }

        public CurrencyApiException(
                String message,
                Throwable cause
        ) {
            super(message, cause);
        }
    }
}