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
        user.setUsername("voice0726");
        user.setViewName("voice0726");
        user.setPassword(encoder.encode("duf4ceye"));
        user.setRoleId(1);
        user.setDeleted(false);
        user.setUuid(UUID.randomUUID());
    }
}
