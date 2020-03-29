package jp.akinori.ecsite.controller.admin;

import jp.akinori.ecsite.entity.Item;
import jp.akinori.ecsite.form.admin.ItemForm;
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
@RequestMapping("/admin/item")
public class ItemController {

    private final AdminService adminService;
    private final AccountService accountService;

    @ModelAttribute("categories")
    public Map<String, String> initializeCategoryMap() {
        return accountService.fetchCategoryNameByUuid();
    }

    @GetMapping("/")
    public String item(Model model, Pageable pageable) {
        Page<Item> items = adminService.fetchAllItems(pageable);
        PageWrapper<Item> page = new PageWrapper<>(items, "/admin/item");
        model.addAttribute("items", items);
        model.addAttribute("page", page);
        return "admin/item/index";
    }

    @GetMapping("/add")
    public String addItemIndex(@ModelAttribute("itemForm") ItemForm itemForm, Model model, Pageable pageable) {
        model.addAttribute("itemForm", itemForm);
        return "admin/item/add";
    }

    @PostMapping("/add")
    public String addItemExec(@ModelAttribute("itemForm") ItemForm itemForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/item/add";
        }
        adminService.createItem(itemForm);
        return "redirect:/admin/item/";
    }

}
