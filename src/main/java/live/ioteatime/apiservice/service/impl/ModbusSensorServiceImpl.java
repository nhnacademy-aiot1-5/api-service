package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.ModbusSensorDto;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.UserNotFoundException;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.UserRepository;
import live.ioteatime.apiservice.service.ModbusSensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ModbusSensorServiceImpl implements ModbusSensorService {

    private final ModbusSensorRepository sensorRepository;
    private final UserRepository userRepository;



    /**
     *
     * @param userId 유저아이디
     * @return 조직이 보유한 센서 리스트를 반환합니다. 없다면 null을 리턴합니다.
     */
    @Override
    public List<ModbusSensorDto> getOrganizationSensorsByUserId(String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        int organizationId = user.getOrganization().getId();

        List<ModbusSensor> sensorList = sensorRepository.findAllByOrganization_Id(organizationId);

        List<ModbusSensorDto> sensorDtoList = new ArrayList<>();
        for(ModbusSensor sensor : sensorList) {
            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(sensor, sensorDto);
            sensorDtoList.add(sensorDto);
        }

        return sensorDtoList;
    }

    @Override
    public ModbusSensorDto getSensorById(int sensorId){
        ModbusSensor sensor = sensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        ModbusSensorDto sensorDto = new ModbusSensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        return sensorDto;
    }
}
