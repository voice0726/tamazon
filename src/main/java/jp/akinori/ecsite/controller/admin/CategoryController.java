package jp.akinori.ecsite.controller.admin;

import jp.akinori.ecsite.entity.Category;
import jp.akinori.ecsite.form.admin.CategoryForm;
import jp.akinori.ecsite.service.AccountService;
import jp.akinori.ecsite.service.AdminService;
import jp.akinori.ecsite.util.PageWrapper;
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

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/category")
public class CategoryController {

    private final AdminService adminService;
    private final AccountService accountService;

    @ModelAttribute("categories")
    public Map<String, String> initializeCategoryMap() {
        return accountService.fetchCategoryNameByUuid();
    }

    @GetMapping("/")
    public String category(Model model, Pageable pageable) {
        Page<Category> categories = adminService.fetchAllCategories(pageable);
        PageWrapper<Category> page = new PageWrapper<>(categories, "/admin/category");
        model.addAttribute("items", categories);
        model.addAttribute("page", page);
        return "admin/category/index";
    }

    @GetMapping("/add")
    public String addCategoryIndex(@ModelAttribute("categoryForm") CategoryForm categoryForm, Model model, Pageable pageable) {
        return "admin/category/add";
    }

    @PostMapping("/add")
    public String addCategoryExec(@ModelAttribute("categoryForm") CategoryForm categoryForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/category/add";
        }
        adminService.createCategory(categoryForm);
        return "redirect:/admin/category/";
    }
}
