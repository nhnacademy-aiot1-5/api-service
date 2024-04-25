package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
