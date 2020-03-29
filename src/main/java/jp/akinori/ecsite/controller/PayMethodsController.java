package jp.akinori.ecsite.controller;

import jp.akinori.ecsite.entity.LoginUser;
import jp.akinori.ecsite.entity.PayMethod;
import jp.akinori.ecsite.form.PayMethodForm;
import jp.akinori.ecsite.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/accounts/paymethods")
@RequiredArgsConstructor
public class PayMethodsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayMethodsController.class);

    private final AccountService accountService;

    @ModelAttribute("user")
    public LoginUser setLoginUser(
            @AuthenticationPrincipal LoginUser user
    ) {
        return user;
    }

    @GetMapping("/")
    public String index(
            @AuthenticationPrincipal LoginUser loginUser,
            Model model
    ) {
        List<PayMethod> payMethodList = accountService.fetchPayMethodsByUserId(loginUser.getUuid());
        model.addAttribute("payMethodList", payMethodList);
        return "accounts/paymethods/index";
    }

    @GetMapping("/add")
    public String create(
            @ModelAttribute("payMethodForm") PayMethodForm form
    ) {
        return "accounts/paymethods/add";
    }

    @PostMapping("/add")
    public String addExec(
            @AuthenticationPrincipal LoginUser user,
            @Validated @ModelAttribute("payMethodForm") PayMethodForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "accounts/paymethods/add";
        }
        PayMethod payMethod = accountService.addPayMethod(form, user.getUuid());
        return "redirect:/accounts/paymethods/";
    }

    @GetMapping("/edit/{uuid}")
    public String edit(
            @PathVariable("uuid") String uuid, Model model,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        PayMethod payMethod = accountService.fetchPayMethodById(UUID.fromString(uuid));
        if (!payMethod.getUserId().equals(loginUser.getUuid())) {
            throw new AccessDeniedException("Access not allowed.");
        }
        PayMethodForm payMethodForm = accountService.convertToForm(payMethod);
        model.addAttribute("payMethodForm", payMethodForm);
        return "accounts/paymethods/edit";
    }

    @PostMapping("/edit")
    public String editExec(
            @Validated @ModelAttribute("payMethodForm") PayMethodForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "accounts/paymethods/edit";
        }
        PayMethod payMethod = accountService.editPayMethod(form);
        return "redirect:/accounts/paymethods/";
    }

    @GetMapping("/delete/{uuid}")
    public String delete(
            @PathVariable("uuid") String uuid
    ) {
        accountService.deletePayMethod(uuid);
        return "redirect:/accounts/paymethods/";
    }
}
