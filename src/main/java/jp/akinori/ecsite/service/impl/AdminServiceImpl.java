package jp.akinori.ecsite.service.impl;

import jp.akinori.ecsite.entity.Category;
import jp.akinori.ecsite.entity.Item;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.form.admin.CategoryForm;
import jp.akinori.ecsite.form.admin.ItemForm;
import jp.akinori.ecsite.form.admin.UserForm;
import jp.akinori.ecsite.repository.CategoryRepository;
import jp.akinori.ecsite.repository.ItemRepository;
import jp.akinori.ecsite.repository.UserRepository;
import jp.akinori.ecsite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public User createUser(UserForm form) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(form.getUsername());
        user.setViewName(form.getViewname());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Page<User> fetchAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Item createItem(ItemForm form) {
        Item item = new Item();
        item.setName(form.getName());
        item.setCategoryId(form.getCategoryId());
        item.setDescription(form.getDescription());
        item.setPrice(form.getPrice());
        item.setStock(form.getStock());
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Page<Item> fetchAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Category createCategory(CategoryForm form) {
        Category category = new Category();
        category.setName(form.getName());
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public Page<Category> fetchAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}
