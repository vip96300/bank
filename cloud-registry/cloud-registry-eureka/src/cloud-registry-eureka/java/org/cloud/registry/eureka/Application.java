package org.cloud.registry.eureka;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心
 *
 */
@EnableEurekaServer
@SpringCloudApplication
public class Application {
	
    public static void main( String[] args ){
    	new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
