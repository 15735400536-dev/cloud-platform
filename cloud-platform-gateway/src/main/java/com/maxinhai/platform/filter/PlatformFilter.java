package com.maxinhai.platform.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PlatformFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String userRole = exchange.getRequest().getHeaders().getFirst("User-Role");

        if ("test".equals(userRole)) {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("Platform-Role-Tag", "test")
                    .build();
            exchange = exchange.mutate().request(mutatedRequest).build();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 确保最早执行
    }

}
