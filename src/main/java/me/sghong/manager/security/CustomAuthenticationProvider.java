package me.sghong.manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (password == null || password.isEmpty()) {
            throw new UsernameNotFoundException("비밀번호를 입력하여 주세요.");
        }

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호를 다시 확인 하여 주세요.");
        }

        if(!user.isEnabled()) {
            throw new BadCredentialsException("로그인에 실패 하였습니다.");
        }

        return new UsernamePasswordAuthenticationToken(username,password,user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
