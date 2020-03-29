package jp.akinori.ecsite.controller.admin;

import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.form.admin.UserForm;
import jp.akinori.ecsite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    private final AdminService adminService;

    @GetMapping("/")
    public String user(@ModelAttribute("userForm") UserForm userForm, Model model, Pageable pageable) {
        Page<User> users = adminService.fetchAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/user/index";
    }

    @GetMapping("/add")
    public String addIndex(@ModelAttribute("userForm") UserForm userForm, Model model, Pageable pageable) {
        Page<User> users = adminService.fetchAllUsers(pageable);
        model.addAttribute("users", users);
        return "admin/user/add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/add";
        }
        adminService.createUser(userForm);
        return "redirect:/admin/user/";
    }
}
