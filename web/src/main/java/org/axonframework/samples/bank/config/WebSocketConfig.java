/*
 * Copyright (c) 2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.bank.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * https://www.jianshu.com/p/4ef5004a1c81
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private Environment environment;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        if (ArrayUtils.contains(environment.getActiveProfiles(), "distributed-command-bus")) {
            config.enableStompBrokerRelay("/topic")
                  .setRelayHost("rabbitmq");
        } else {
            //定义了一个客户端订阅地址的前缀信息，也就是客户端接收服务端发送消息的前缀信息 $stomp.subscribe
            config.enableSimpleBroker("/topic");
        }
        //定义了服务端接收地址的前缀，也即客户端给服务端发消息的地址前缀 $stomp.send
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //添加一个/websocket 端点，客户端就可以通过这个端点来进行连接；withSockJS作用是添加SockJS支持
        registry.addEndpoint("/websocket")
                .withSockJS();
    }
}