package jp.akinori.ecsite.helper;

import jp.akinori.ecsite.entity.LoginUser;
import jp.akinori.ecsite.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User principal = new User();
        principal.setUsername(user.username());
        principal.setUuid(UUID.fromString(user.uuid()));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        context.setAuthentication(auth);
        return context;
    }
}
