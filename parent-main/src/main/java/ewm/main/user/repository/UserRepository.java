package ewm.main.user.repository;

import ewm.main.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(List<Long> ids);

    List<User> findByIdBetween(Long start, Long end);
}
