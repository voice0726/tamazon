package jp.akinori.ecsite.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    public void testEncode() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPass = bCryptPasswordEncoder.encode("duf4ceye");
        System.out.println(encodedPass);
    }
}
