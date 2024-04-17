package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Role;
import live.ioteatime.apiservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<User,String> {
    List<User> findAllByRole(Role role);
}
