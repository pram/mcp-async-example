package com.naughtyzombie.ai.mcp_client;

import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringAIClient {
    private final ChatClient.Builder chatClientBuilder;
    private final List<McpAsyncClient> mcpAsyncClients;
    private final ToolCallbackProvider tools;

    public SpringAIClient(ChatClient.Builder chatClientBuilder, List<McpAsyncClient> mcpAsyncClients, ToolCallbackProvider tools) {
        this.chatClientBuilder = chatClientBuilder;
        this.mcpAsyncClients = mcpAsyncClients;
        this.tools = tools;
    }

    public void getChat() {

        var chatClient = getChatClient();

        System.out.println(chatClient.prompt("calculateHello James").call().content());

    }

    public void getChatStream() {

        var chatClient = getChatClient();

        chatClient.prompt("calculateHello James").stream().content().subscribe(System.out::println);

    }

    private ChatClient getChatClient() {
        //For some reason the ToolCallbackProvider bean is creating 2 Asyncclients
        ToolCallbackProvider toolCallbackProvider2 = new AsyncMcpToolCallbackProvider(mcpAsyncClients.get(0));

        return chatClientBuilder
                .defaultTools(toolCallbackProvider2)
                .build();
    }
}
