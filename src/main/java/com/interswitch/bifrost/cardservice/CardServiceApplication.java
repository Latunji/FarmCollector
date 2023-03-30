package com.interswitch.bifrost.cardservice;
import java.util.concurrent.Executor;

import com.interswitch.bifrost.commons.async.ContextAwarePoolExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.interswitch.bifrost.commons.adapter.CommonConfigurationAdapter;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EnableAsync

public class CardServiceApplication extends CommonConfigurationAdapter{

    static {
      LogManager.getLogManager().reset();
      SLF4JBridgeHandler.install();
  }
    public static void main(String[] args) {
        
        
            SpringApplication.run(CardServiceApplication.class, args);
    }

    @Bean(name="threadPool")
    public Executor asyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ContextAwarePoolExecutor();
        executor.setCorePoolSize(13);
        executor.setMaxPoolSize(13);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("threadAsync");
       // executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(0);
                
        executor.initialize();
        return executor;
    }

}


