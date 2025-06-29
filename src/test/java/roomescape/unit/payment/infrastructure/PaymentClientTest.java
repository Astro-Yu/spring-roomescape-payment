package roomescape.unit.payment.infrastructure;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import roomescape.payment.dto.request.PaymentRequest;
import roomescape.payment.dto.response.PaymentResponse;
import roomescape.payment.infrastructure.PaymentClient;

@ExtendWith(MockitoExtension.class)
public class PaymentClientTest {

    private PaymentClient paymentClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        paymentClient = new PaymentClient(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("성공적으로 결제 승인 요청을 보내고 응답을 반환합니다.")
    void approveSuccess() {
        // given
        PaymentRequest request = new PaymentRequest("테스트 오더 id 1", "테스트 결제 키 1", 1000);
        String mockJson = """
                    {
                        "orderId": "테스트 오더 id 1",
                        "paymentKey": "테스트 결제 키 1",
                        "totalAmount": 1000
                    }
                """;
        mockServer.expect(requestTo("https://api.tosspayments.com/v1/payments/confirm"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        // when
        PaymentResponse response = paymentClient.approve(request);

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.paymentKey()).isEqualTo("테스트 결제 키 1");
        soft.assertThat(response.orderId()).isEqualTo("테스트 오더 id 1");
        soft.assertThat(response.totalAmount()).isEqualTo(1000);
        soft.assertAll();
    }
}
