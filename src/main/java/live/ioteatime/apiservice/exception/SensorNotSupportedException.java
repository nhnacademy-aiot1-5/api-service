package live.ioteatime.apiservice.exception;

public class SensorNotSupportedException extends RuntimeException {
    public SensorNotSupportedException() {
        super("지원하지 않는 디바이스입니다.");
    }
}
