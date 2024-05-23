package live.ioteatime.apiservice.aop;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.UnauthorizedException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.MqttSensorRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
    private final ModbusSensorRepository modbusSensorRepository;
    private final PlaceRepository placeRepository;

    @Before("@annotation(live.ioteatime.apiservice.annotation.AdminOnly)")
    public void hasAdminRole() {
        User user = getUserFromHeader();
        Role role = user.getRole();
        if (!role.equals(Role.ADMIN)) {
            throw new UnauthorizedException();
        }
    }

    @Pointcut("execution(* live.ioteatime.apiservice.controller.ModbusSensorController.*(..)) || " +
            "execution(* live.ioteatime.apiservice.controller.ChannelController.*(..))")
    public void modbusPointcut() {
    }

    @Pointcut("execution(* live.ioteatime.apiservice.controller.MqttSensorController.*(..)) || " +
            "execution(* live.ioteatime.apiservice.controller.TopicController.*(..))")
    public void mqttPointcut() {
    }

    @Pointcut("@annotation(live.ioteatime.apiservice.annotation.VerifyOrganization)")
    public void verifyOrganizationPointcut() {
    }

    @Before("verifyOrganizationPointcut() && (args(organizationId,*) || args(organizationId))")
    public void checkOrganizationMatch(int organizationId) {
        checkOrganizationMatchesWithUserOrganization(organizationId);
    }

    /**
     * 요청 헤더의 X-USER-ID로 검색한 유저가 속한 조직과, 요청 URL PathVariable 중 sensorId로 검색한 센서가 속한 조직이 일치하는지 체크합니다.
     * 일치하지 않으면 UnAuthorizedException을 던집니다.
     *
     * @param sensorId 센서아이디
     */
    @Before("mqttPointcut() && verifyOrganizationPointcut() && args(sensorId,*,*)")
    public void checkOrganizationMatchFotMqtt(int sensorId) {

        Organization sensorOrganization = mqttSensorRepository.findById(sensorId)
                .orElseThrow(SensorNotFoundException::new).getOrganization();
        if (Objects.isNull(sensorOrganization)) {
            throw new OrganizationNotFoundException();
        }

        checkOrganizationMatchesWithUserOrganization(sensorOrganization.getId());

    }

    @Before("modbusPointcut() && verifyOrganizationPointcut() && args(sensorId,*,*)")
    public void checkOrganizationMatchForModbus(int sensorId) {

        Organization sensorOrganization = modbusSensorRepository.findById(sensorId)
                .orElseThrow(SensorNotFoundException::new).getOrganization();
        if (Objects.isNull(sensorOrganization)) {
            throw new OrganizationNotFoundException();
        }

        checkOrganizationMatchesWithUserOrganization(sensorOrganization.getId());
    }

    /**
     * http 요청의 "X-USER-ID" 헤더로 유저를 검색합니다.
     *
     * @return User 엔티티
     */
    private User getUserFromHeader() {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        String userId = httpServletRequest.getHeader("X-USER-ID");
        if (userId == null) {
            throw new UnauthorizedException();
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * 유저가 소속된 조직을 검색합니다.
     *
     * @param user 유저 엔티티
     * @return Organization 엔티티
     */
    private int getUserOrganizationId(User user) {
        Organization organization = user.getOrganization();
        if (Objects.isNull(organization)) {
            throw new OrganizationNotFoundException();
        }
        return organization.getId();
    }

    private void checkOrganizationMatchesWithUserOrganization(int organizationId) {
        int userOrgId = getUserOrganizationId(getUserFromHeader());
        if (userOrgId != organizationId) {
            throw new UnauthorizedException();
        }
    }
}
