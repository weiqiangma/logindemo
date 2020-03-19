package com.example.logindemo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

@Slf4j
@Builder
public class JwtTokenUtil {
    //传输信息，必须是json格式
    private String msg;
    //所验证的jwt
    @Setter
    private String token;

    private final String secret="324iu23094u598ndsofhsiufhaf_+0wq-42q421jiosadiusadiasd";

    /**
     * 生成加密Token
     * @return
     */
    public String creatJwtToken(){
        msg = new AESUtil(msg).encrypt();
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("mwq")  //签发者
                    .withIssuedAt(DateTime.now().toDate())  //签发时间
                    .withExpiresAt(DateTime.now().plusDays(1).toDate())  //过期时间
                    .withClaim("user",msg)
                    .sign(Algorithm.HMAC256(secret));  //设置签名算法及秘钥
        }catch (Exception e){
            log.info("生成Token出错： "+e);
            e.printStackTrace();
        }
        log.info("加密后Token: " + token);
        return token;
    }

    public String freeJwt(){
        DecodedJWT decodedJWT = null;
        try {
            //使用hmac256加密算法，因为默认加密使用的也是该算法
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("mwq")
                    .build();
            decodedJWT = verifier.verify(token);
            log.info("签名人：" + decodedJWT.getIssuer() + "加密方式：" + decodedJWT);
        }catch (Exception e){
            log.info("Token解析出错：" + e);
            e.printStackTrace();
        }

        String [] headPayload = token.split("\\.");
        String header = decodedJWT.getHeader();
        String payload = decodedJWT.getPayload();
        if(!header.equals(headPayload[0]) && !payload.equals(headPayload[1])){
            log.info("验证出错");
        }
        return new AESUtil(decodedJWT.getClaim("user").asString()).decrypt();
    }

    public static void main(String[] args){
        String token=JwtTokenUtil.builder().msg("hello").build().creatJwtToken();
        String token_text=JwtTokenUtil.builder().token(token).build().freeJwt();
        log.info(token_text);
    }
}
