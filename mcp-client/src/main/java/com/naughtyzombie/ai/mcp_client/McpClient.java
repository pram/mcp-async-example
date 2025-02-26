package com.naughtyzombie.ai.mcp_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import org.springframework.ai.autoconfigure.mcp.client.NamedClientMcpTransport;
import org.springframework.ai.autoconfigure.mcp.client.configurer.McpAsyncClientConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

public class McpClient {

	public void run() {

		var asyncClient = getMcpAsyncClient();

		asyncClient.initialize().subscribe(x -> System.out.println("Initialize Response: " + x));

		asyncClient.ping().subscribe(x -> System.out.println("Ping Response: " + x));

		asyncClient.listTools()
				.doOnNext(tools -> tools.tools().forEach(tool ->
						System.out.println(tool.name())))
				.subscribe();

		asyncClient.callTool(new CallToolRequest("getHello", Map.of(
				"name", "Mr Client MCP"))).subscribe(x -> {
					x.content().forEach(content -> {
						System.out.println("Content: " + content);
					});
				});

		asyncClient.closeGracefully();
	}

	public static McpAsyncClient getMcpAsyncClient() {
		ObjectMapper objectMapper = new ObjectMapper();
		var webClientBuilder = WebClient.builder().baseUrl("http://localhost:8123");

		var transport = new WebFluxSseClientTransport(webClientBuilder, objectMapper);
		NamedClientMcpTransport namedTransport = new NamedClientMcpTransport("mcp-server-from-client", transport);
		McpSchema.Implementation clientInfo = new McpSchema.Implementation("MCP Schema", "1.0.0");

		io.modelcontextprotocol.client.McpClient.AsyncSpec asyncSpec = io.modelcontextprotocol.client.McpClient.async(transport)
				.clientInfo(clientInfo)
				.requestTimeout(Duration.ofSeconds(30));
		final McpAsyncClientConfigurer mcpAsyncClientConfigurer = new McpAsyncClientConfigurer(Collections.emptyList());
		asyncSpec = mcpAsyncClientConfigurer.configure(namedTransport.name(), asyncSpec);

		var asyncClient = asyncSpec.build();
		return asyncClient;
	}

}