package jp.akinori.ecsite.util;

import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@SpringBootTest
public class CreateTestUser {
    @Autowired
    UserRepository repository;

    @Test
    public void insertTestUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("test2");
        user.setViewName("test2");
        user.setPassword(encoder.encode("123456"));
        user.setRoleId(1);
        user.setDeleted(false);
        user.setUuid(UUID.randomUUID());

        repository.saveAndFlush(user);
    }
}
