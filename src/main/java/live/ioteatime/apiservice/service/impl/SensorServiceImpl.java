package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotSupportedException;
import live.ioteatime.apiservice.repository.AdminRepository;
import live.ioteatime.apiservice.repository.SensorRepository;
import live.ioteatime.apiservice.repository.SupportedSensorRepository;
import live.ioteatime.apiservice.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SupportedSensorRepository supportedSensorRepository;
    private final AdminRepository adminRepository;

    @Override
    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
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
