package live.ioteatime.apiservice.properties;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ioteatime.user")
@Getter
public class UserProperties {
    private String userDetailUri;
}
