package com.iceps.spring.disruptor.example.other;

import com.iceps.spring.disruptor.common.MainTemplate;
import com.iceps.spring.disruptor.common.generic.GenericEvent;
import com.iceps.spring.disruptor.common.generic.GenericExceptionEventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * Created by yanglikun on 2017/2/17.
 */
public class SimulationExceptionMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericExceptionEventHandler<String>("handler-1"));
        disruptor.setDefaultExceptionHandler(new SimpleExceptionHandler());
    }

    public static void main(String[] args) {
        new SimulationExceptionMain().run();
    }

    private static class SimpleExceptionHandler implements ExceptionHandler {
        public void handleEventException(Throwable ex, long sequence, Object event) {
            System.err.println("处理发生异常:" + event);
        }

        public void handleOnStartException(Throwable ex) {
            System.err.println("启动发生异常:" + ex.getMessage());
            ex.printStackTrace();
        }

        public void handleOnShutdownException(Throwable ex) {
            System.err.println("停止发生异常:" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
