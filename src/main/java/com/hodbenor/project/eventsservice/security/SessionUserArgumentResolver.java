package com.hodbenor.project.eventsservice.security;

import com.hodbenor.project.eventsservice.dao.beans.User;
import com.hodbenor.project.eventsservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger LOG = LoggerFactory.getLogger(SessionUserArgumentResolver.class);

    private static final String MISSING_AUTH_TOKEN_ERROR_STRING = "Missing auth token in header";
    public static final String HEADER_AUTH_TOKEN = "Auth-Token";
    private final UserService userService;
    private final TokenService tokenService;

    public SessionUserArgumentResolver(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory binderFactory)
            throws Exception {

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        String authToken = request.getHeader(HEADER_AUTH_TOKEN);
        if (authToken == null) {
            LOG.error(MISSING_AUTH_TOKEN_ERROR_STRING);
            throw new UnAuthorizedException("Missing auth token in header");
        }

        try {
            if (!tokenService.validToken(authToken)) {
                throw new UnAuthorizedException("Invalid auth token");
            }

            return userService.findUserByToken(authToken)
                    .orElseThrow(() -> new UnAuthorizedException("User not fount"));
        } catch (Exception e) {
            throw new UnAuthorizedException(String.format("Invalid login token %s", authToken));
        }
    }
}
