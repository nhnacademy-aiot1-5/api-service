package live.ioteatime.apiservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("api-서비스 API 문서")
                .version("v0.1.0")
                .description("api-서비스의 API문서 모음집입니다.");

        return new OpenAPI()
                .info(info);
    }
    
}
