package live.ioteatime.apiservice.advice;

import live.ioteatime.apiservice.exception.SensorNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdminControllerAdvice {
    @ExceptionHandler(SensorNotSupportedException.class)
    public ResponseEntity<String> sensorNotSupported(SensorNotSupportedException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
