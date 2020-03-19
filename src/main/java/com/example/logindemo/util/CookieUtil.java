package com.example.logindemo.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    /**
     * 获取Cookie值
     * @param request
     * @param key
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if(ArrayUtils.isNotEmpty(cookies)){
            for(Cookie cookie : cookies){
                if(StringUtils.equals(cookie.getName(),key)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static Cookie genCookieWithDomain(String key, String value, int maxAge, String domain){
        Cookie cookie = new Cookie(key,value);
        cookie.setDomain(domain);
        enrichCookie(cookie,"/",maxAge);
        return cookie;
    }

    /**
     * 生成Cookie
     * @param key
     * @param value
     * @param uri
     * @param maxAge
     * @return
     */
    public static Cookie genCookie(String key, String value, String uri, int maxAge){
        Cookie cookie = new Cookie(key, value);
        enrichCookie(cookie, uri, maxAge);
        return  cookie;
    }

    /**
     * cookie设置生存时间
     * @param cookie
     * @param uri
     * @param maxAge
     */
    public static void enrichCookie(Cookie cookie, String uri, int maxAge) {
        cookie.setPath(uri);
        cookie.setMaxAge(maxAge);
    }

}
