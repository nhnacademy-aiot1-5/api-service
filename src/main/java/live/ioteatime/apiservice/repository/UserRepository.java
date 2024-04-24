package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
}
