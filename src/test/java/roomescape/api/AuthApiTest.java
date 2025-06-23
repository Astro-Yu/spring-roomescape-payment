package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.member.domain.Credentials;
import roomescape.member.domain.Member;
import roomescape.member.domain.Name;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AuthApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("올바른 계정으로 로그인을 시도합니다.")
    void loginTest() {
        Member member = new Member(null, new Name("홍길동"), new Credentials("ama@gmail.com", "ama1233333"), Role.ADMIN,
                false);
        Member savedMember = memberRepository.save(member);

        // given
        Map<String, String> request = new HashMap<>();
        request.put("email", savedMember.getCredentials().getEmail());
        request.put("password", savedMember.getCredentials().getPassword());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/auth/sign-in")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 후 로그아웃을합니다.")
    void logoutTest() {
        Member member = new Member(null, new Name("홍길동"), new Credentials("ama@gmail.com", "ama1233333"), Role.ADMIN,
                false);
        Member savedMember = memberRepository.save(member);

        // given
        Map<String, String> request = new HashMap<>();
        request.put("email", savedMember.getCredentials().getEmail());
        request.put("password", savedMember.getCredentials().getPassword());

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/auth/sign-in")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().post("/api/auth/sign-out")
                .then().log().all()
                .statusCode(200);
    }
}
