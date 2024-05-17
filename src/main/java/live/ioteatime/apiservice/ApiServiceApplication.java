package live.ioteatime.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiServiceApplication.class, args);
    }
}
