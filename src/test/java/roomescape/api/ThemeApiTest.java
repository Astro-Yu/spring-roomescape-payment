package roomescape.api;

import io.restassured.RestAssured;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.dto.response.ThemeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql(value = "/sql/Theme.sql")
public class ThemeApiTest {

    @Test
    @DisplayName("모든 테마를 조회합니다.")
    void getAllThemes() {
        //when & then
        List<ThemeResponse> responses = RestAssured.given().log().all()
                .when().get("api/themes")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ThemeResponse.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(4);
        soft.assertThat(responses.getFirst().id()).isEqualTo(1L);
        soft.assertAll();
    }
}
