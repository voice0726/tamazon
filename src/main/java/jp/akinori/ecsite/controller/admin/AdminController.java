package jp.akinori.ecsite.controller.admin;

import jp.akinori.ecsite.service.AccountService;
import jp.akinori.ecsite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AccountService accountService;

    @ModelAttribute("categories")
    public Map<String, String> initializeCategoryMap() {
        return accountService.fetchCategoryNameByUuid();
    }

    @RequestMapping("/")
    public String index() {
        return "admin/index";
    }
}
