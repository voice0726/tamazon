package jp.akinori.ecsite.service;

import jp.akinori.ecsite.entity.Item;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.form.admin.ItemForm;
import jp.akinori.ecsite.form.admin.UserForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    User createUser(UserForm form);

    Page<User> fetchAllUsers(Pageable pageable);

    Item createItem(ItemForm form);

    Page<Item> fetchAllItems(Pageable pageable);

}
