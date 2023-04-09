package com.jmd.rx.callback;

import com.jmd.rx.Topic;
import io.reactivex.rxjava3.subjects.PublishSubject;

public interface OnSubscribeCallback {

    void exec(Topic topic, PublishSubject<Object> subject);

}
