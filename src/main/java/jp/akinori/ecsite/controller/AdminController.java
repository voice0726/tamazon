package jp.akinori.ecsite.controller;

import jp.akinori.ecsite.entity.Item;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.form.admin.ItemForm;
import jp.akinori.ecsite.form.admin.UserForm;
import jp.akinori.ecsite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @RequestMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/user")
    public String user(@ModelAttribute("userForm") UserForm userForm, Model model, Pageable pageable) {
        Page<User> users = adminService.fetchAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/user/index";
    }

    @GetMapping("/user/add")
    public String addIndex(@ModelAttribute("userForm") UserForm userForm, Model model, Pageable pageable) {
        Page<User> users = adminService.fetchAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/user/add";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/add";
        }
        adminService.createUser(userForm);
        return "redirect:/admin/user";
    }

    @GetMapping("/item")
    public String item(@ModelAttribute("itemForm") ItemForm itemForm, Model model, Pageable pageable) {
        Page<Item> items = adminService.fetchAllItems(pageable);
        model.addAttribute("items", items);
        return "admin/item/index";
    }

    @GetMapping("/item/add")
    public String addItemIndex(@ModelAttribute("itemForm") ItemForm itemForm, Model model, Pageable pageable) {
        Page<User> users = adminService.fetchAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/user/add";
    }

    @PostMapping("/item/add")
    public String addItemExec(@ModelAttribute("itemForm") ItemForm itemForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/item/add";
        }
        adminService.createItem(itemForm);
        return "redirect:/admin/item";
    }
}
