package StepDefinitions;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
		//config.setApplicationDestinationPrefixes("/topic");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// with sockjs
		registry.addEndpoint("/ws-log").setAllowedOrigins("http://localhost:3000", "http://localhost:8080").withSockJS();
		// without sockjs
		//registry.addEndpoint("/ws-message").setAllowedOriginPatterns("*");
	}
	
}