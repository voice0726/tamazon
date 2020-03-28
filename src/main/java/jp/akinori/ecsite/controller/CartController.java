package jp.akinori.ecsite.controller;

import jp.akinori.ecsite.entity.CartItem;
import jp.akinori.ecsite.entity.LoginUser;
import jp.akinori.ecsite.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @RequestMapping("/")
    public String index(@AuthenticationPrincipal LoginUser user, Pageable pageable, Model model) {
        Page<CartItem> cartItems = cartService.fetchCartItemsByUserId(user.getUuid(), pageable);
        model.addAttribute(cartItems);
        return "cart/index";
    }
}
