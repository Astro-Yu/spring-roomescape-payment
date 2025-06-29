package roomescape.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.payment.dto.response.PaymentResponse;
import roomescape.payment.infrastructure.PaymentClient;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.infrastructure.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/sql/Member.sql", "/sql/ReservationTime.sql", "/sql/Theme.sql"})
public class UserReservationApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private PaymentClient paymentClient;

    private String sessionId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        Map<String, String> request = new HashMap<>();
        request.put("email", "river@gmail.com");
        request.put("password", "river123");

        sessionId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/auth/sign-in")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("JSESSIONID");
    }

    @Test
    @DisplayName("회원이 자신의 예약을 생성합니다.")
    void createMyReservation() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, Object> request = new HashMap<>();
        request.put("date", date.toString());
        request.put("timeId", "1");
        request.put("themeId", "1");
        request.put("paymentKey", "123");
        request.put("orderId", "456");
        request.put("amount", 1000);

        PaymentResponse paymentResponse = new PaymentResponse("456", "123", 1000);

        given(paymentClient.approve(any())).willReturn(paymentResponse);

        // when & then
        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201)
                .body("date", equalTo(date.toString()))
                .body("memberName", equalTo("리버"));
    }
}
