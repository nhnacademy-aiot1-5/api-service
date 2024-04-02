package live.ioteatime.apiservice.aop;

import com.sun.net.httpserver.Headers;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.exception.UnauthorizedException;
import live.ioteatime.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {
    private final UserRepository userRepository;

    @Before("@annotation(live.ioteatime.apiservice.annotation.AdminOnly)")
    public void doRoleCheck() {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        Role role = userRepository.findRoleById(httpServletRequest.getHeader("X-USER-ID"));

        if (!role.equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
    }
}
