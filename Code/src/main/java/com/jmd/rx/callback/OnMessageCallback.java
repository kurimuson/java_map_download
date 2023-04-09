package com.jmd.rx.callback;

public interface OnMessageCallback<T> {

    void exec(T msg);

}
