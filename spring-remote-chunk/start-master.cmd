@echo off
CHCP 65001
setlocal enabledelayedexpansion
title master

set CLASSPATH=spring-remote-chunk-0.0.1-SNAPSHOT.jar
set CLASSPATH=%CLASSPATH%;lib/activemq-client-5.8.0.jar
set CLASSPATH=%CLASSPATH%;lib/activemq-pool-5.8.0.jar
set CLASSPATH=%CLASSPATH%;lib/aopalliance-1.0.jar
set CLASSPATH=%CLASSPATH%;lib/commons-logging-1.1.3.jar
set CLASSPATH=%CLASSPATH%;lib/commons-pool-1.6.jar
set CLASSPATH=%CLASSPATH%;lib/geronimo-j2ee-management_1.1_spec-1.0.1.jar
set CLASSPATH=%CLASSPATH%;lib/geronimo-jms_1.1_spec-1.1.1.jar
set CLASSPATH=%CLASSPATH%;lib/geronimo-jta_1.0.1B_spec-1.0.1.jar
set CLASSPATH=%CLASSPATH%;lib/hawtbuf-1.9.jar
set CLASSPATH=%CLASSPATH%;lib/jcl-over-slf4j-1.6.4.jar
set CLASSPATH=%CLASSPATH%;lib/jettison-1.1.jar
set CLASSPATH=%CLASSPATH%;lib/junit-4.8.2.jar
set CLASSPATH=%CLASSPATH%;lib/log4j-1.2.17.jar
set CLASSPATH=%CLASSPATH%;lib/logback-classic-1.1.7.jar
set CLASSPATH=%CLASSPATH%;lib/logback-core-1.1.7.jar
set CLASSPATH=%CLASSPATH%;lib/slf4j-api-1.7.20.jar
set CLASSPATH=%CLASSPATH%;lib/slf4j-log4j12-1.7.6.jar
set CLASSPATH=%CLASSPATH%;lib/spring-aop-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-batch-core-2.2.7.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-batch-infrastructure-2.2.7.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-batch-integration-1.3.1.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-beans-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-context-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-context-support-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-core-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-expression-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-integration-core-3.0.2.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-integration-jms-3.0.2.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-jms-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-retry-1.0.2.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-test-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/spring-tx-3.2.9.RELEASE.jar
set CLASSPATH=%CLASSPATH%;lib/xpp3_min-1.1.4c.jar
set CLASSPATH=%CLASSPATH%;lib/xstream-1.3.jar

java -cp %CLASSPATH% -Dspring.profiles.active=master com.iceps.spring.launch.Application