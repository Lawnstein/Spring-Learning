package com.iceps.spring.disruptor.example.basic;

import com.iceps.spring.disruptor.common.MainTemplate;
import com.iceps.spring.disruptor.common.generic.GenericEvent;
import com.iceps.spring.disruptor.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 链式
 * step1、step2、step3会按照顺序处理
 * <p>
 * Created by yanglikun on 2017/2/16.
 */
public class DependenciesChainMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1"))
                .then(new GenericEventHandler<String>("step2"))
                .then(new GenericEventHandler<String>("step3"));
    }

    public static void main(String[] args) {
        new DependenciesChainMain().run();
    }
}
