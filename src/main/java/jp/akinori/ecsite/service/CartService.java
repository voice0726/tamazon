package jp.akinori.ecsite.service;

import jp.akinori.ecsite.entity.Cart;
import jp.akinori.ecsite.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CartService {

    Page<CartItem> fetchCartItemsByUserId(UUID cartId, Pageable pageable);

    Cart createNewByUserId(UUID userId);

    Cart addItem(UUID itemId);

    Cart updateCart(UUID itemId, int count);

    Cart deleteItem(UUID itemId);

}
