package jp.akinori.ecsite.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class LoginUser extends User {
    private UUID uuid;
    private String username;
    private String viewName;

    /**
     *
     *
     * @param user
     * @param authorities
     */
    public LoginUser(jp.akinori.ecsite.entity.User user, Collection<? extends GrantedAuthority> authorities){
        super(user.getUsername(), user.getPassword(), authorities);
        this.uuid = user.getUuid();
        this.username = user.getUsername();
        this.viewName = user.getViewName();

    }
}
