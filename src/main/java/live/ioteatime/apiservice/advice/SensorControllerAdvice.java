package live.ioteatime.apiservice.advice;

import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotSupportedException;
import live.ioteatime.apiservice.exception.TopicNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SensorControllerAdvice {

    @ExceptionHandler(SensorNotSupportedException.class)
    public ResponseEntity<String> sensorNotSupported(SensorNotSupportedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(SensorNotFoundException.class)
    public ResponseEntity<String> sensorNotFound(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<String> topicNotFound(){
        return ResponseEntity.notFound().build();
    }
}
