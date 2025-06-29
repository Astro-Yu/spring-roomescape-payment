package roomescape.payment.infrastructure;

import java.util.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.dto.response.PaymentResponse;

@Component
public class PaymentClient {

    private static final String URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String TEST_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";


    private final RestTemplate restTemplate;

    public PaymentClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentResponse approve(PaymentRequest paymentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",
                String.format("%s %s", "Basic", Base64.getEncoder().encodeToString(TEST_KEY.getBytes())));
        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);

        return restTemplate.postForObject(URL, request, PaymentResponse.class);
    }
}
