package com.jmd.rx.client.impl;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.callback.OnMessageCallback;
import com.jmd.rx.callback.OnSubscribeCallback;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.HashMap;
import java.util.Map;

public class NormalInnerMqClient implements InnerMqClient {

    public Map<Topic, PublishSubject<Object>> subjects = new HashMap<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final String id;
    private OnSubscribeCallback onSubscribeCallback;
    private boolean destroyed = false;

    public NormalInnerMqClient(String id) {
        this.id = id;
    }

    @Override
    public void setOnSubscribeCallback(OnSubscribeCallback onSubscribeCallback) {
        this.onSubscribeCallback = onSubscribeCallback;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public PublishSubject<Object> getSubject(Topic topic) {
        return this.subjects.get(topic);
    }

    @Override
    public boolean isDestroyed() {
        return this.destroyed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sub(Topic topic, OnMessageCallback<T> callback) {
        var subject = subjects.computeIfAbsent(topic, k -> PublishSubject.create());
        this.compositeDisposable.add(subject.subscribe((msg) -> {
            callback.exec((T) msg);
        }));
        this.onSubscribeCallback.exec(topic, subject);
    }

    @Override
    public void destroy() {
        this.destroyed = true;
        this.subjects.clear();
        if (!this.compositeDisposable.isDisposed()) {
            this.compositeDisposable.dispose();
        }
    }

}





