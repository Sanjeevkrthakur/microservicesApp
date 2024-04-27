package com.example.questionservice.filter;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints= List.of(
            "/auth.register",
            "/auth/token",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndpoints
                    .stream()
                    .noneMatch(url -> serverHttpRequest.getURI().getPath().contains(url));


    public  boolean isSecured(ServerHttpRequest request) {
        return isSecured.test(request);
    }


}
