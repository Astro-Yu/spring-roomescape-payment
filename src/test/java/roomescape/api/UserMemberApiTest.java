package roomescape.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.infrastructure.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/sql/Member.sql")
public class UserMemberApiTest {

    private String sessionId;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        // given
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
    @DisplayName("회원가입을 진행합니다.")
    void createMember() {
        // given
        Map<String, String> request = new HashMap<>();
        request.put("name", "김수한무");
        request.put("email", "email@gmail.com");
        request.put("password", "123456789");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("api/members/sign-up")
                .then().log().all()
                .statusCode(201);

        assertThat(memberRepository.existsByCredentials_Email("email@gmail.com")).isTrue();
    }

    @Test
    @DisplayName("자신의 멤버 정보를 조회합니다.")
    void getMyMember() {

        // when & then
        MemberResponse response = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().get("/api/members/my")
                .then().log().all()
                .statusCode(200)
                .extract().as(MemberResponse.class);

        SoftAssertions soft = new SoftAssertions();

        soft.assertThat(response.id()).isEqualTo(1L);
        soft.assertThat(response.name()).isEqualTo("아마");
        soft.assertThat(response.email()).isEqualTo("ama@gmail.com");
        soft.assertAll();
    }

    @Test
    void deleteMyMember() {
        //when & then
        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().delete("/api/members/my")
                .then().log().all()
                .statusCode(204);
    }
}
