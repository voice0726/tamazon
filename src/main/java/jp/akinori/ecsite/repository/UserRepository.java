package jp.akinori.ecsite.repository;

import jp.akinori.ecsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String userName);

    Optional<User> findByUuid(UUID uuid);
}
