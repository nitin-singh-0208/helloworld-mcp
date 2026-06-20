package com.demo.mcp.hello;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloMcpServerApplication.class, args);
    }

    @Bean
    ToolCallbackProvider helloTools(HelloToolService helloToolService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(helloToolService)
                .build();
    }
}
