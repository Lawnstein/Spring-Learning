package com.iceps.spring.disruptor.example.basic;

import com.iceps.spring.disruptor.common.MainTemplate;
import com.iceps.spring.disruptor.common.generic.GenericEvent;
import com.iceps.spring.disruptor.common.generic.GenericEventHandler;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 菱形
 * step1-1、step1-2是并行执行的<br/>
 * step1-1和step1-2都执行完之后step2才会执行
 * <p>
 * Created by yanglikun on 2017/2/16.
 */
public class DependenciesDiamondMain extends MainTemplate {

    public void addHandler(Disruptor<GenericEvent<String>> disruptor) {
        disruptor.handleEventsWith(new GenericEventHandler<String>("step1-1"),
                new GenericEventHandler<String>("step1-2"))
                .then(new GenericEventHandler<String>("step2"));//这里是用了then
    }

    public static void main(String[] args) {
        new DependenciesDiamondMain().run();
    }
}
