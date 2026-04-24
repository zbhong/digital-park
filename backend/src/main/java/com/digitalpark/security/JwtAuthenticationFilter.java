package com.digitalpark.security;

import com.digitalpark.common.utils.JwtUtils;
import com.digitalpark.common.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * 从请求头中提取 Token，验证并设置认证信息
 *
 * @author digitalpark
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final RedisUtils redisUtils;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            token = authHeader.substring(TOKEN_PREFIX.length());
            try {
                username = jwtUtils.getUsernameFromToken(token);
            } catch (Exception e) {
                log.warn("JWT Token解析失败: {}", e.getMessage());
            }
        }

        // 如果用户名不为空且当前未认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 检查 Token 是否在黑名单中（登出时加入）
            String blacklistKey = "token:blacklist:" + token;
            if (Boolean.TRUE.equals(redisUtils.hasKey(blacklistKey))) {
                log.warn("Token已被加入黑名单，用户: {}", username);
                filterChain.doFilter(request, response);
                return;
            }

            // 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 验证 Token 是否有效
            if (jwtUtils.validateToken(token, username)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 设置认证信息到安全上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.debug("用户 {} 认证成功，设置安全上下文", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
