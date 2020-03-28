package jp.akinori.ecsite.repository;

import jp.akinori.ecsite.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Page<CartItem> findAllByCartId(UUID cartId, Pageable pageable);
}
