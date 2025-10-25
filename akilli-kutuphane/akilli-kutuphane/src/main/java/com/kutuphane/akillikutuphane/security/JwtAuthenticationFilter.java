package com.kutuphane.akillikutuphane.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 1. JWT'yi HTTP isteği başlığından (Header) alma
        String token = getJwtFromRequest(request);

        // 2. Token'ı doğrulama
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            
            // Token geçerliyse, kullanıcı adını al
            String username = tokenProvider.getUsernameFromJWT(token);

            // 3. Kullanıcıyı yükleme (UserDetailsService kullanarak)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 4. Kimlik doğrulama nesnesini oluşturma
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Spring Security Bağlamına yerleştirme (Kullanıcının giriş yaptığını belirtir)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // HTTP başlığından JWT token'ı çıkarma
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Token 'Bearer <token>' formatında olmalıdır
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 'Bearer ' kısmını atla
        }
        return null;
    }
}