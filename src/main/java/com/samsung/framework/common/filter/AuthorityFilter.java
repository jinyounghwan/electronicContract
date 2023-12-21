package com.samsung.framework.common.filter;

import com.samsung.framework.common.enums.AuthorityEnum;
import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.exception.CustomJwtException;
import com.samsung.framework.common.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
public class AuthorityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String REISSUE = "reissue";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String reissue = request.getHeader(REISSUE) != null ? request.getHeader(REISSUE) : "N";

        // token 전송 여부 확인
        if(authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            log.error(ResultCodeMsgEnum.NO_HEADER_AUTHORIZATION.getMsg());
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        request.setAttribute(MapKeyStringEnum.TOKEN_REISSUE.getKeyString(), reissue);

        // Token expiration status
        try {
            jwtUtil.isExpired(token);
            jwtUtil.isInvalidToken(token, reissue);
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw new CustomJwtException(ExceptionCodeMsgEnum.EXPIRED_TOKEN);
        } catch (SignatureException ex) {
            log.error(ex.getMessage());
            throw new CustomJwtException(ExceptionCodeMsgEnum.INVALID_TOKEN_SIGNATURE);
        }

        // Get request user information out of a token
//        String userName = jwtUtil.getUserName(token);
//        String userAuthority = jwtUtil.getUserAuthority(token);
//        var authorityList = new ArrayList<SimpleGrantedAuthority>();
//        if(!StringUtils.hasText(userAuthority)) {
//            authorityList.add(new SimpleGrantedAuthority(AuthorityEnum.NO_AUTHORITY.getValue()));
//        }else {
//            authorityList.add(new SimpleGrantedAuthority(userAuthority));
//        }
//
//        // Grant Authentication
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorityList);
//
//        // Set details
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
