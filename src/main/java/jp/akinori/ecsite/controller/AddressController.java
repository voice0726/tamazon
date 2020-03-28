package jp.akinori.ecsite.controller;

import jp.akinori.ecsite.entity.Address;
import jp.akinori.ecsite.entity.LoginUser;
import jp.akinori.ecsite.form.AddressForm;
import jp.akinori.ecsite.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accounts/addresses/")
@RequiredArgsConstructor
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    private final AccountService accountService;

    @GetMapping("/")
    public String index(
            Model model, @AuthenticationPrincipal LoginUser user
    ) {
        List<Address> addressList = accountService.fetchAddressesByUserId(user.getUuid());
        model.addAttribute(addressList);
        return "accounts/addresses/index";
    }

    @GetMapping("/add")
    public String create(
            @ModelAttribute("addressForm") AddressForm form
    ) {
        return "accounts/addresses/add";
    }

    @PostMapping("/add")
    public String addExec(
            @Validated @ModelAttribute("addressForm") AddressForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser user
    ) {
        if (bindingResult.hasErrors()) {
            return "accounts/addresses/edit";
        }
        Address address = accountService.addAddress(form, user.getUuid());
        return "redirect:/accounts/addresses/";
    }

    @GetMapping("/edit/{uuid}")
    public String edit(
            @PathVariable("uuid") String uuid, Model model
    ) {
        AddressForm addressForm = accountService.createAddressForm(uuid);
        model.addAttribute("addressForm", addressForm);
        return "accounts/addresses/edit";
    }

    @PostMapping("/edit")
    public String editExec(
            @Validated @ModelAttribute("addressForm") AddressForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "accounts/addresses/edit";
        }
        Address address = accountService.editAddress(form);
        return "redirect:/accounts/addresses/";
    }

    @GetMapping("/delete/{addressId}")
    public String delete(@PathVariable("addressId") String addressId) {
        accountService.deleteAddress(addressId);
        return "redirect:/accounts/addresses/";
    }
}
