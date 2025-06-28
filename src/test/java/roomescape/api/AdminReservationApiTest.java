package roomescape.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/sql/Member.sql", "/sql/ReservationTime.sql", "/sql/Theme.sql"})
public class AdminReservationApiTest {

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
    
    private String sessionId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        Map<String, String> request = new HashMap<>();
        request.put("email", "ama@gmail.com");
        request.put("password", "ama1233333");

        sessionId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/auth/sign-in")
                .then().log().all()
                .statusCode(200)
                .extract().cookie("JSESSIONID");
    }

    @Test
    @DisplayName("예약 생성 테스트")
    void createReservation() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Map<String, String> request = new HashMap<>();
        request.put("date", date.toString());
        request.put("timeId", "1");
        request.put("themeId", "1");
        request.put("memberId", "1");

        // when & then
        ReservationResponse response = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().as(ReservationResponse.class);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("조건에 따라 예약을 검색합니다.")
    void searchReservationByConditions() {
        // given
        LocalDate date1 = LocalDate.of(2025, 5, 5);
        ReservationTime time = reservationTimeRepository.findById(1L).get();

        Theme theme1 = themeRepository.findById(1L).get();
        Theme theme2 = themeRepository.findById(2L).get();

        Member member1 = memberRepository.findById(1L).get();
        Member member2 = memberRepository.findById(2L).get();

        Reservation reservation1 = Reservation.createWithoutId(date1, time, theme1, member1);
        Reservation reservation2 = Reservation.createWithoutId(date1, time, theme2, member1);

        Reservation reservation3 = Reservation.createWithoutId(date1, time, theme1, member2);
        Reservation reservation4 = Reservation.createWithoutId(date1, time, theme2, member2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        // when
        List<ReservationResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().get("/api/admin/reservations/search?memberId=1&themeId=2")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationResponse.class);

        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(1);
        soft.assertThat(responses.getFirst().id()).isEqualTo(2L);
    }
}
