package live.ioteatime.apiservice.aop;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.exception.RefererNotMatchesException;
import live.ioteatime.apiservice.exception.UnauthorizedException;
import live.ioteatime.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class BeforeExecuteMethodAspect {
    private final UserRepository userRepository;

    @Before("@annotation(live.ioteatime.apiservice.annotation.AdminOnly)")
    public void hasAdminRole() {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        Role role = userRepository.findRoleById(httpServletRequest.getHeader("X-USER-ID"));

        if (!role.equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
    }

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void areRefererAndHeaderSame() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("ServletRequestAttributes 또는 요청이 존재하지 않습니다.");
        }

        HttpServletRequest request = attributes.getRequest();

        String referer = request.getHeader("referer");
        String host = request.getHeader("host");

        if (!host.equals(referer)) {
            throw new RefererNotMatchesException("referer가 host와 일치하지 않습니다." + "referer : " + referer + " host : " + host);
        }
    }
}
