package com.piuraexpressa.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setMaxPostSize(15 * 1024 * 1024); // 15 MB
            connector.setProperty("maxSwallowSize", "-1");
            connector.setProperty("fileCountMax", "100"); // Permite hasta 100 archivos
        });
    }
}
