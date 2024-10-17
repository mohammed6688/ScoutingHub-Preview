package com.krnal.products.scoutinghub.authorization;

import com.krnal.products.scoutinghub.security.UserSessionHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    @Around("@annotation(requiresRoles)")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, RequiresRoles requiresRoles) throws Throwable {
        String[] requiredRoles = requiresRoles.value();
        boolean hasAccess = false;

        // Check if the current user has any of the required roles
        for (String role : requiredRoles) {
            if (UserSessionHelper.checkUserAccess(role)) {
                hasAccess = true;
                break;
            }
        }

        if (!hasAccess) {
            throw new AccessDeniedException("Access denied: insufficient permissions.");
        }

        // Proceed with the original method if authorized
        return joinPoint.proceed();
    }
}