package jp.akinori.ecsite.service.impl;

import jp.akinori.ecsite.entity.Cart;
import jp.akinori.ecsite.entity.CartItem;
import jp.akinori.ecsite.repository.CartItemRepository;
import jp.akinori.ecsite.repository.CartRepository;
import jp.akinori.ecsite.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Page<CartItem> fetchCartItemsByUserId(UUID userId, Pageable pageable) {

        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart = optionalCart.orElseGet(() -> createNewByUserId(userId));

        UUID cartId = cart.getUuid();
        return cartItemRepository.findAllByCartId(cartId, pageable);
    }

    @Override
    @Transactional
    public Cart createNewByUserId(UUID userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setDeleted(false);
        return cartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart addItem(UUID itemId) {
        return null;
    }

    @Override
    public Cart updateCart(UUID itemId, int count) {
        return null;
    }

    @Override
    public Cart deleteItem(UUID itemId) {
        return null;
    }
}
