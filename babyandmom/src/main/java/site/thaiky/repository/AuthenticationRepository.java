package site.thaiky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.thaiky.entity.User;


@Repository
public interface AuthenticationRepository extends JpaRepository<User, Long> {

}
