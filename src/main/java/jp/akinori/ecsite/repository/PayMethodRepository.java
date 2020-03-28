package jp.akinori.ecsite.repository;

import jp.akinori.ecsite.entity.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayMethodRepository extends JpaRepository<PayMethod, UUID> {
    List<PayMethod> findAllByUserIdAndDeletedFalse(UUID userId);

    Optional<PayMethod> findByDefaultMethodTrue();

    Optional<PayMethod> findByUuid(UUID uuid);
}
