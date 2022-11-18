package com.karmai.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhaokang03
 * @Date 2022/11/15 13:16
 */
public class JwtUtils {
    private static final String TOKEN_SECRET = "F3935E69-BD8B-4D77-AA5D-74B5BD6B4300";

    public static String genToken(String userName) {
        try {
            // 设置过期时间
            Date date = DateUtils.addSeconds(new Date(), 24 * 60 * 60);
            // 设置私钥加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "JWT");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withIssuedAt(new Date())
                    .withClaim("userName", userName)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 检验token是否正确
     *
     * @param token
     * @return
     */
    public static String verify(String token) {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userName").asString();
    }
}
