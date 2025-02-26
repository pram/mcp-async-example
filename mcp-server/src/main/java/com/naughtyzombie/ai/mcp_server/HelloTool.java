package com.naughtyzombie.ai.mcp_server;

import org.springframework.ai.tool.annotation.Tool;

public class HelloTool {
    @Tool(name="calculateHello", description = "Say Hello to the person whose name is given")
    public String calculateHello(String name) {
        System.out.println("calculateHello called with name: " + name);
        return String.format("Hello %s me old mucker", name);
    }
}
