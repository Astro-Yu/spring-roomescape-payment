package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.infrastructure.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql(value = "/sql/Theme.sql")
public class ThemeApiTest {

    @Autowired
    private ThemeRepository themeRepository;

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

    @Test
    @DisplayName("테마를 생성합니다.")
    void createTheme() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("name", "흉흉한 테마");
        request.put("description", "흉흉한 설명");
        request.put("thumbnail", "흉흉한 섬네일");

        // when & then
        ThemeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/themes")
                .then().log().all()
                .statusCode(201)
                .extract().as(ThemeResponse.class);
        
        List<Theme> themes = themeRepository.findAll();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(themes).hasSize(5);
        soft.assertThat(response.id()).isEqualTo(5L);
        soft.assertAll();
    }
}
