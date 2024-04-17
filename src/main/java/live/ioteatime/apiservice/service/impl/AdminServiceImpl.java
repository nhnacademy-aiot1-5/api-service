package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.dto.UserDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotSupportedException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.AdminRepository;
import live.ioteatime.apiservice.repository.SensorRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final SensorRepository sensorRepository;
    private final SupportedSensorRepository supportedSensorRepository;

    @Override
    public List<UserDto> getGuestUsers() {
        List<User> users = adminRepository.findAllByRole(Role.GUEST);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto, "password");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = adminRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto, "password", "");
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public UserDto updateUserRole(String userId) {
        User user = adminRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setRole(Role.USER);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        adminRepository.save(user);
        return userDto;
    }

    /**
     * 서버에서 지원하는 센서 모델인지 체크 후 등록 가능한 경우, 센서를 데이터베이스에 등록합니다.
     * @param userId 어드민 유저 아이디
     * @param request 센서 등록 요청 폼 데이터
     * @return
     */
    @Override
    public int addMqttSensor(String userId, AddSensorRequest request) {

        log.debug("Add sensor request - model_name: {}", request.getModelName());
        if(!supportedSensorRepository.existsByModelName(request.getModelName())){
            throw new SensorNotSupportedException();
        }
        Organization organization = adminRepository.findById(userId).get().getOrganization();
        if(Objects.isNull(organization)) {
            throw new OrganizationNotFoundException();
        }

        Sensor sensor = new Sensor();
        BeanUtils.copyProperties(request, sensor);
        sensor.setAlive(Alive.DOWN);
        sensor.setOrganization(organization);

        sensorRepository.save(sensor);

        return sensor.getId();
    }
}
