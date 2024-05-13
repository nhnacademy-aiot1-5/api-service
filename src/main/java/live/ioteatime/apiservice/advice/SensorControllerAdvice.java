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
    public ResponseEntity<String> sensorNotFound(SensorNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<String> topicNotFound(TopicNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> illegalAccess(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalState(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
