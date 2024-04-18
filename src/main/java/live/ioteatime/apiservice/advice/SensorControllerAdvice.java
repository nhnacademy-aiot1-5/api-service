package live.ioteatime.apiservice.advice;

import live.ioteatime.apiservice.exception.SensorNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SensorControllerAdvice {

    @ExceptionHandler(SensorNotFoundException.class)
    public ResponseEntity<String> sensorNotFound(){
        return ResponseEntity.notFound().build();
    }
}
