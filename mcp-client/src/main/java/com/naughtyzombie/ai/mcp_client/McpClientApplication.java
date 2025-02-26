package com.naughtyzombie.ai.mcp_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpClientApplication {

	private static Logger log = LoggerFactory
			.getLogger(McpClientApplication.class);

	public static void main(String[] args) {
		log.info("Start McpClientApplication");
		SpringApplication.run(McpClientApplication.class, args);
		log.info("End McpClientApplication");
	}

	@Bean
	@ConditionalOnProperty(name = "app.runner.mode", havingValue = "mcp")
	public CommandLineRunner conditionalRunnerMcp() {
		return args -> {
			new McpClient().run();
		};
	}

	@Bean
	@ConditionalOnProperty(name = "app.runner.mode", havingValue = "standard")
	public CommandLineRunner conditionalRunnerSpringStandard(SpringAIClient config) {
		return args -> {
			config.getChat();
		};
	}

	@Bean
	@ConditionalOnProperty(name = "app.runner.mode", havingValue = "stream")
	public CommandLineRunner conditionalRunnerSpringStream(SpringAIClient config) {
		return args -> {
			config.getChatStream();
		};
	}
}
