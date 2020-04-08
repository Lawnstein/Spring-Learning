package com.iceps.spring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger( HelloController.class );

    @Value( "${server.port}" )
    String port;


    @Value( "${jdbc.url}" )
    String url;

    @Value( "${plat.cache.interval.millis}" )
    String timeout;
    
    @GetMapping("hi")
    public String hi(String name) {
        logger.debug( "debug log... {}, {}, {}", port, url, timeout );
        logger.info( "info log... {}, {}, {}", port, url, timeout );
        logger.warn( "warn log... {}, {}, {}", port, url, timeout );
        return "hi " + name + " , i am from port:" + port + ", current jdbc url " + url + ", timeout : " + timeout;
    }

}
