package jp.akinori.ecsite.controller;

import jp.akinori.ecsite.entity.User;
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
    @ResponseBody
    public ResponseEntity<Object> addUser(@ModelAttribute("userForm") UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
