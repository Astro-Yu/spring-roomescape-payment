package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.response.ReservationTimeResponse;
import roomescape.reservationTime.infrastructure.ReservationTimeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql(value = "/sql/ReservationTime.sql")
public class ReservationTimeApiTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("모든 예약시간을 조회합니다.")
    void findAllReservationTimes() {

        // when & then
        List<ReservationTimeResponse> responses = RestAssured.given().log().all()
                .when().get("api/reservation-times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationTimeResponse.class);
        
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(11);
        soft.assertThat(responses.getFirst().id()).isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @DisplayName("예약 시간을 생성합니다.")
    void createReservationTimes() {
        //given
        Map<String, String> request = new HashMap<>();
        request.put("startAt", "21:00");

        // when
        ReservationTimeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("api/reservation-times")
                .then().log().all()
                .statusCode(201)
                .extract().as(ReservationTimeResponse.class);

        //then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(reservationTimes).hasSize(12);
        soft.assertThat(response.id()).isEqualTo(12L);
        soft.assertThat(response.startAt()).isEqualTo(LocalTime.of(21, 0));
        soft.assertAll();
    }

    @Test
    @DisplayName("예약 시간을 삭제합니다.")
    void deleteReservationTime() {
        // when
        RestAssured.given().log().all()
                .when().delete("api/reservation-times/1")
                .then().log().all()
                .statusCode(204);
        // then
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(reservationTimes).hasSize(10);
        soft.assertThat(reservationTimes.stream()
                        .anyMatch(reservationTime -> reservationTime.getId().equals(1L)))
                .isFalse();
        soft.assertAll();
    }
}
