package me.sghong.manager.security;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.service.AdminService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomAuthenticationService implements UserDetailsService {
    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("아이디를 입력하여 주세요.");
        }

        AdminDto adminDto = adminService.getAdminInfoForGroup(username);

        if (adminDto == null) {
            throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");
        }
        if (adminDto.getPwderrcnt() >= 5) {
            throw new BadCredentialsException("해당 계정은 잠겼습니다.");
        }

        return User.builder()
                .username(adminDto.getAdminid())
                .password(adminDto.getAdminpwd())
                .roles("USER")
                .build();
    }
}
