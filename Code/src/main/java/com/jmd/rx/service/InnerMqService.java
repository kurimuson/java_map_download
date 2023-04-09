package com.jmd.rx.service;

import com.jmd.rx.Topic;
import com.jmd.rx.client.InnerMqClient;
import com.jmd.rx.client.impl.NormalInnerMqClient;
import com.jmd.util.CommonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InnerMqService {

    private static volatile InnerMqService instance;
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4, 16, 16, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>() // 使用无界任务队列，线程池的任务队列可以无限制的添加新的任务
    );

    private final Random random = new Random(123456789); // 使用种子随机数生成唯一ID
    private final Map<String, InnerMqClient> clients = new HashMap<>(); // 客户端
    private final Map<Topic, Map<String, InnerMqClient>> topic2clients = new HashMap<>(); // 根据Topic存储的客户端

    /* 建立连接 */
    public InnerMqClient createClient() throws Exception {
        var id = CommonUtils.generateCharMixed(20) + "_" + this.random.nextInt();
        if (this.clients.get(id) != null) {
            this.createClient();
        }
        return this.createClient(id);
    }

    /* 建立连接 */
    public InnerMqClient createClient(String id) throws Exception {
        if (this.clients.get(id) != null) {
            throw new Exception("Client ID重复");
        }
        var client = new NormalInnerMqClient(id);
        client.setOnSubscribeCallback((topic, subject) -> {
            // 根据topic存储client
            this.topic2clients.computeIfAbsent(topic, k -> new HashMap<>());
            this.topic2clients.get(topic).put(id, client);
        });
        this.clients.put(id, client);
        return client;
    }

    /* 销毁连接 */
    public void destroyClient(InnerMqClient client) {
        if (client == null) {
            return;
        }
        // 删除客户端
        this.clients.remove(client.getId());
        // 删除根据Topic存储的客户端
        for (Topic topic : this.topic2clients.keySet()) {
            this.topic2clients.get(topic).remove(client.getId());
        }
        client.destroy();
    }

    /* 全局发送 */
    public <T> void pub(Topic topic, T msg) {
        var clients = this.topic2clients.get(topic);
        if (clients != null) {
            for (InnerMqClient client : clients.values()) {
                threadPoolExecutor.execute(new Thread(() -> this.pubToClient(client, topic, msg)));
            }
        }
    }

    /* 指定发送 */
    public void pub(String id, Topic topic, Object msg) {
        if (id.indexOf("*") == 0) {
            var innerId = id.replace("*", "");
            for (var entry : this.clients.entrySet()) {
                if (entry.getKey().contains(innerId)) {
                    threadPoolExecutor.execute(new Thread(() -> this.pubToClient(entry.getValue(), topic, msg)));
                }
            }
        } else {
            for (var entry : this.clients.entrySet()) {
                if (entry.getKey().equals(id)) {
                    threadPoolExecutor.execute(new Thread(() -> this.pubToClient(entry.getValue(), topic, msg)));
                    break;
                }
            }
        }
    }

    private <T> void pubToClient(InnerMqClient client, Topic topic, T msg) {
        if (!client.isDestroyed()) {
            var subject = client.getSubject(topic);
            if (subject != null) {
                subject.onNext(msg);
            }
        }
    }

    public static InnerMqService getInstance() {
        if (instance == null) {
            synchronized (InnerMqService.class) {
                if (instance == null) {
                    instance = new InnerMqService();
                }
            }
        }
        return instance;
    }

}
