package com.naughtyzombie.ai.mcp_server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    HelloTool helloTool() {
        return new HelloTool();
    }

    @Bean
    public ToolCallbackProvider getTools(HelloTool helloTool) {
        return MethodToolCallbackProvider.builder().toolObjects(helloTool).build();
    }
}
