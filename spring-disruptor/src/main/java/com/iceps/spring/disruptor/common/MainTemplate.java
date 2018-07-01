package com.iceps.spring.disruptor.common;

import java.util.Scanner;
import java.util.concurrent.Executors;

import com.iceps.spring.disruptor.common.generic.GenericEvent;
import com.iceps.spring.disruptor.common.generic.GenericEventFactory;
import com.iceps.spring.disruptor.common.generic.GenericEventProducer;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 提供公用的模板，方便测试。
 * 在控制台输入消息内容回车,输入exit退出程序
 * Created by yanglikun on 2017/2/16.
 */
public abstract class MainTemplate {


    public void run() {
        GenericEventFactory<GenericEvent<String>> eventFactory = new GenericEventFactory<GenericEvent<String>>();
        int bufferSize = 1024;
        Disruptor<GenericEvent<String>> disruptor = new Disruptor(eventFactory, bufferSize,
                Executors.defaultThreadFactory());

        addHandler(disruptor);

        disruptor.start();

        RingBuffer<GenericEvent<String>> ringBuffer = disruptor.getRingBuffer();

        doAfterDisruptorStart(ringBuffer);

        GenericEventProducer<String> producer = new GenericEventProducer(ringBuffer);

        for (; ; ) {
            Scanner scan = new Scanner(System.in);
            String msg = scan.nextLine();
            if ("exit".equals(msg)) {
                System.exit(0);
            }
            producer.onData(msg);
        }
    }

    protected void doAfterDisruptorStart(final RingBuffer<GenericEvent<String>> ringBuffer) {

    }

    public abstract void addHandler(Disruptor<GenericEvent<String>> disruptor);
}
