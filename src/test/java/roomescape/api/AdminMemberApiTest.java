package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
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
import roomescape.member.domain.Member;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.infrastructure.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Sql("/sql/Member.sql")
public class AdminMemberApiTest {

    @Autowired
    private MemberRepository memberRepository;
    private String sessionId;

    @BeforeEach
    void setUp() {
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
    @DisplayName("모든 멤버 조회")
    void getAllMembers() {
        // when & then
        List<MemberResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().get("api/admin/members")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MemberResponse.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(4);
        soft.assertThat(responses.getFirst().id()).isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @DisplayName("삭제된 멤버 조회")
    void getAllDeletedMembers() {
        // when & then
        List<MemberResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().get("api/admin/members?status=deleted")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MemberResponse.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(2);
        soft.assertThat(responses.getFirst().id()).isEqualTo(3L);
        soft.assertAll();
    }

    @Test
    @DisplayName("존재하는 멤버 조회")
    void getAllActiveMembers() {
        // when & then
        List<MemberResponse> responses = RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().get("api/admin/members?status=active")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", MemberResponse.class);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(responses).hasSize(2);
        soft.assertThat(responses.getFirst().id()).isEqualTo(1L);
        soft.assertAll();
    }

    @Test
    @DisplayName("관리자가 특정 회원을 id로 삭제")
    void deleteMember() {
        // when & then
        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .when().delete("api/admin/members/1")
                .then().log().all()
                .statusCode(204);

        List<Member> members = memberRepository.findAll();
        Member deletedMember = members.stream()
                .filter(member -> member.getId().equals(1L))
                .findFirst()
                .get();
        
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(members).hasSize(4);
        soft.assertThat(deletedMember.isDeleted()).isTrue();
        soft.assertAll();
    }
}
