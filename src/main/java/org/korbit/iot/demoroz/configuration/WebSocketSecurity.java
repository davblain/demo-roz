package org.korbit.iot.demoroz.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurity extends AbstractSecurityWebSocketMessageBrokerConfigurer{
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpSubscribeDestMatchers("user/**").hasAuthority("ROLE_USER")
                .simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
                .anyMessage().denyAll();
    }
}
