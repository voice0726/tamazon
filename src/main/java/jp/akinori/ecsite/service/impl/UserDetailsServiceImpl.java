package jp.akinori.ecsite.service.impl;

import jp.akinori.ecsite.entity.LoginUser;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(userName);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User" + userName + "was not found in the database");
        }

        User user = userOptional.get();

        List<GrantedAuthority> grantList = new ArrayList<>();
        String role = user.getRole().getName();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        grantList.add(authority);

        return new LoginUser(user, grantList);
    }
}
