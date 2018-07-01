package com.iceps.spring.disruptor.example.basic;

import com.iceps.spring.disruptor.common.MainTemplate;
import com.iceps.spring.disruptor.common.generic.GenericEvent;
import com.iceps.spring.disruptor.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * step1、step2、step3 之间 并行处理没有先后顺序
 * Created by yanglikun on 2017/2/15.
 */
public class ParallelEventHandlersMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1"),
                new GenericEventHandler<String>("step2")
                , new GenericEventHandler<String>("step3"));
    }

    public static void main(String[] args) {
        new ParallelEventHandlersMain().run();
    }


}
