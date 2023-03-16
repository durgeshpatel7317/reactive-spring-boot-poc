package com.example.demo.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class CustomSubscriber<T> implements Subscriber<T> {
    private Subscription subscription;
    private long count = 0;

    @Override
    public void onSubscribe(Subscription s) {
        log.info("Subscription is done");
        this.subscription = s;
        this.subscription.request(1);
    }

    @Override
    public void onNext(T t) {
        log.info("On next is called {}", t);
        count++;
        if (count >= 1) {
            count = 0;
            subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
