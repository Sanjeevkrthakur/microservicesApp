package com.example.questionservice.filter;

import com.example.questionservice.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
//    @Autowired
//    private RestTemplate template;

    @Autowired
    private JwtService jwtService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!validator.isSecured((org.springframework.http.server.ServerHttpRequest) request)) {
                throw new RuntimeException("Missing Authorization header");
            }
//            if (!validator.isSecured().test(exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION),true)){
//                throw new RuntimeException("Missing Authorization header");
//            }
            String authHeaders = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeaders != null && authHeaders.startsWith("Bearer")) {
                authHeaders = authHeaders.substring(7); // Assign the result of substring back to authHeaders
            }
            try {
                //template.getForObject("http://localhost:8765/identity-service/auth/validate?token=" + authHeaders, String.class);

                jwtService.validateToken(authHeaders);

            } catch (Exception e) {
                System.out.println("Invalid access....!!");
                throw new RuntimeException("Unauthorized access to application");
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Any configuration properties can be added here
    }
}
