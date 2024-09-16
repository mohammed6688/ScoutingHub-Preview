package com.krnal.products.scoutinghub.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public class UserSessionHelper {

    private static Map<String ,Object> getClaims(){
        Jwt jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return jwt.getClaims();
    }
    public static String getUserId(){
        return ((String) getClaims().get("preferred_username"));
    }

    public static String getUserName(){
        return ((String) getClaims().get("given_name"));
    }

    @SuppressWarnings("all")
    public static Boolean checkUserAccess(String role){
        List<String> roles = (List<String>) ((Map<String,Object>) getClaims().get("realm_access")).get("roles");
        return roles.contains(role);
    }
}
