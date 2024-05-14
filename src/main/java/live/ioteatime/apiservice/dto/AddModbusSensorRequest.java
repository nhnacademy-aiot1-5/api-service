package live.ioteatime.apiservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddModbusSensorRequest {
    //센서 아이디
    private String name;

    //ip 주소
    private String host;

    //채널 {function-code}/{address}/{type}
    private String channel;
}
