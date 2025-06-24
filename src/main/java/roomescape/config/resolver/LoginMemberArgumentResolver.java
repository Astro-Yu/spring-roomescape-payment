package roomescape.config.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.exception.InvalidAuthorizationException;
import roomescape.config.dto.SessionMember;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 컨트롤러 메서드의 특정 파라미터를 지원하는지 판단 파라미터에 @Login이 붙어있고, 파라미터 클래스 타입이 SessionMember.class 인 경우 true를 반환
     **/
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean isSessionMemberClass = SessionMember.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isSessionMemberClass;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new InvalidAuthorizationException();
        }
        SessionMember sessionMember = (SessionMember) session.getAttribute("LOGIN_MEMBER");
        if (sessionMember == null) {
            throw new InvalidAuthorizationException();
        }
        return sessionMember;
    }
}
