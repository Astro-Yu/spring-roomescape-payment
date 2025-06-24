package roomescape.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.InvalidAuthorizationException;
import roomescape.config.dto.SessionMember;

public class AdminApiAuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new InvalidAuthorizationException();
        }
        SessionMember sessionMember = (SessionMember) session.getAttribute("LOGIN_MEMBER");
        if (sessionMember == null) {
            throw new InvalidAuthorizationException();
        }

        return true;
    }
}
