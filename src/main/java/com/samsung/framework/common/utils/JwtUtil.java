//package com.samsung.framework.common.utils;
//
//import com.samsung.framework.common.enums.ExceptionCodeMsgEnum;
//import com.samsung.framework.common.enums.MapKeyStringEnum;
//import com.samsung.framework.common.enums.RequestTypeEnum;
//import com.samsung.framework.common.enums.TableNameEnum;
//import com.samsung.framework.common.exception.CustomJwtException;
//import com.samsung.framework.domain.common.TokenObjectVO;
//import com.samsung.framework.domain.token.Token;
//import com.samsung.framework.mapper.token.TokenMapper;
//import com.samsung.framework.vo.common.TokenVO;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Date;
//
///**
// * JWT 유틸
// */
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//    @Value("${jwt.issuer}")
//    private String issuer;
//
//    private final ValidationUtil validationUtil;
//    private final TokenMapper tokenMapper;
//
//    @PostConstruct
//    protected void encodeKey() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//    /**
//     * 토큰 객체 생성
//     * @param userName {@link String}
//     * @param accessExpiresAt {@link Long}
//     * @param refreshExpiresAt {@link Long}
//     * @param auth {@link Arrays}
//     * @return {@link TokenObjectVO}
//     */
//    public TokenObjectVO createTokenObject(String userName, Long accessExpiresAt, Long refreshExpiresAt, String auth) {
//        String accessToken = this.generateJwt(userName, accessExpiresAt, auth);
//        String refreshToken = this.generateJwt(userName, refreshExpiresAt, auth);
//
//        // 토큰정보 생성 및 저장
//        String tokenSeq = tokenMapper.findTokenSeq(userName);
//        String logType;
//        if(!StringUtils.hasText(tokenSeq)) {
//            tokenSeq = "0";
//            logType = RequestTypeEnum.CREATE.getRequestType();
//        }else {
//            logType = RequestTypeEnum.UPDATE.getRequestType();
//        }
//
//        Token target = Token.builder()
//                .memberId(userName)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .tableName(TableNameEnum.TOKEN.name())
//                .logId1(tokenSeq)
//                .logType(logType)
//                .regId(userName)
//                .build();
//
//        validationUtil.parameterValidator(target, Token.class);
//        int isTokenExist = tokenMapper.isTokenExist(userName);
//        if(isTokenExist < 1) {
//            tokenMapper.insert(target);
//        }else {
//            tokenMapper.update(target);
//        }
//
//        return TokenObjectVO.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .key(userName)
//                .logId1(userName)
//                .tableName(TableNameEnum.TOKEN.name())
//                .regId(userName)
//                .build();
//    }
//
//    /**
//     * JWT 생성
//     * @param userName {@link String}
//     * @param expiredMs {@link Long}
//     * @return {@link String}
//     */
//    public String generateJwt(String userName, Long expiredMs, String auth) {
//        Claims claims = Jwts.claims().setSubject(userName);
//        claims.put(MapKeyStringEnum.JWT_USERNAME.getKeyString(), userName);
//        claims.put(MapKeyStringEnum.AUTH.getKeyString(), auth);
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
//                .setIssuer(issuer)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    /**
//     * JWT 만료 여부
//     * @param token {@link String}
//     */
//    public void isExpired(String token) {
//        boolean isExpired = Jwts.parserBuilder().setSigningKey(secretKey).build()
//                .parseClaimsJws(token).getBody().getExpiration().before(new Date());
//        if (isExpired) {
//            throw new CustomJwtException(ExceptionCodeMsgEnum.EXPIRED_TOKEN);
//        }
//    }
//
//    /**
//     * 유효 토큰(JWT) 여부
//     *
//     * @param token   {@link String}
//     * @param reissue
//     */
//    public void isInvalidToken(String token, String reissue) {
//        boolean validToken = tokenMapper.isValidToken(token, reissue);
//        if(!validToken) {
//            throw new CustomJwtException(ExceptionCodeMsgEnum.INVALID_TOKEN);
//        }
//    }
//
//    /**
//     * JWT 유저 명 조회
//     *
//     * @param token {@link String}
//     * @return {@link String}
//     */
//    public String getUserName(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey).build()
//                .parseClaimsJws(token).getBody().get(MapKeyStringEnum.JWT_USERNAME.getKeyString(), String.class);
//    }
//
//    /**
//     * JWT 유저 권한 정보 조회
//     * @param token {@link String}
//     * @return {@link String}
//     * @throws JsonProcessingException
//     */
//    public String getUserAuthority(String token) throws JsonProcessingException {
//        TokenVO tokenVO = this.decode(token);
//        return tokenVO.getAuthority().stream().toList().get(0).getValue();
//    }
//
//    /**
//     * JWT decoder
//     * @param token {@link String}
//     * @return {@link TokenVO}
//     * @throws JsonProcessingException
//     */
//    public TokenVO decode(String token) throws JsonProcessingException {
//        if(!StringUtils.hasText(token)) {
//            throw new NullPointerException(ExceptionCodeMsgEnum.NO_TOKEN.getMsg());
//        }
//
//        String[] chunk = token.split("\\.");
//        Base64.Decoder decoder = Base64.getDecoder();
//        String payload = new String(decoder.decode(chunk[1]));
//        log.info("Token payload: {}", payload);
//
//        ObjectMapper mapper = new ObjectMapper();
//        TokenVO tokenVO = mapper.readValue(payload, TokenVO.class);
//
//        return tokenVO;
//    }
//}
