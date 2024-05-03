package live.ioteatime.apiservice.aop;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.UnauthorizedException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.MqttSensorRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BeforeExecuteMethodAspect {
    private final UserRepository userRepository;
    private final MqttSensorRepository mqttSensorRepository;

    @Before("@annotation(live.ioteatime.apiservice.annotation.AdminOnly)")
    public void hasAdminRole() {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String userId = httpServletRequest.getHeader("X-USER-ID");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Role role = user.getRole();

        if (!role.equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
    }

    /**
     * 요청 헤더의 X-USER-ID로 검색한 유저가 속한 조직과, 요청 URL PathVariable 중 sensorId로 검색한 센서가 속한 조직이 일치하는지 체크합니다.
     * 일치하지 않으면 UnAuthorizedException을 던집니다.
     * @param sensorId 센서아이디
     */
    @Before("@annotation(live.ioteatime.apiservice.annotation.VerifyOrganization) && args(*,sensorId,*)")
    public void checkOrganizationMatch(int sensorId){

        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String userId = httpServletRequest.getHeader("X-USER-ID");

        Organization userOrganization = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)).getOrganization();
        if(Objects.isNull(userOrganization)){
            throw new OrganizationNotFoundException();
        }
        int userOrgId = userOrganization.getId();

        Organization sensorOrganization = mqttSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new).getOrganization();
        if(Objects.isNull(sensorOrganization)){
            throw new OrganizationNotFoundException();
        }
        int sensorOrgId = sensorOrganization.getId();

        if (userOrgId != sensorOrgId) {
            throw new UnauthorizedException();
        }

    }

}
