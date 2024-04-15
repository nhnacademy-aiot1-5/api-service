package live.ioteatime.apiservice.aop;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.exception.UnauthorizedException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.AdminRepository;
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
    private final AdminRepository adminRepository;

    @Before("@annotation(live.ioteatime.apiservice.annotation.AdminOnly)")
    public void hasAdminRole() {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String userId = httpServletRequest.getHeader("X-USER-ID");
        User user = adminRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Role role = user.getRole();

        if (!role.equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
    }
}
