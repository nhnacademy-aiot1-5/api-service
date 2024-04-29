package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateChannelNameRequest {
    //여기에 자바 기본 자료형인 int를 사용할 경우에는 null값을 허용할 수 없기 때문에 해당 자료형의 객체 래버 클래스인 Integer를 사용하면
    //null을 허용할 수 있음
    private Integer id;
    @JsonProperty("channel_name")
    private String channelName;
}
