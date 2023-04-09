import { Injectable } from '@angular/core';
import { Observable, Subject } from "rxjs";
import { Topic } from "../topic";
import { InnerMqClient } from "../client/inner-mq.client";
import { NormalInnerMqClient } from "../client/impl/normal-inner-mq.client";
import { Random } from "../util/random";
import { LoInnerMqClient } from "../client/impl/lo-inner-mq.client";

@Injectable()
export class InnerMqService {

	private random = new Random(); // 使用种子随机数生成唯一ID
	private clients = new Map<string, InnerMqClient>(); // 客户端
	private topic2clients = new Map<Topic, Map<string, InnerMqClient>>(); // 根据Topic存储的客户端
	private persistentQueue = new Map<any, Array<{ type: PersistentType, data: any }>>(); // 持久化队列

	constructor() {
	}

	/* 创建连接 */
	public createClient(id?: string): InnerMqClient {
		return this.createClientByParam({ id: id, clazz: NormalInnerMqClient });
	}

	/* 创建lo连接 */
	public createLoClient(id?: string): InnerMqClient {
		return this.createClientByParam({ id: id, clazz: LoInnerMqClient });
	}

	private createClientByParam(param: {
		id?: string,
		clazz: typeof NormalInnerMqClient | typeof LoInnerMqClient
	}): InnerMqClient {
		if (param.id == null) {
			param.id = Random.generateCharMixed(20) + '_' + this.random.nextInt(2147483647);
		}
		if (this.clients.has(param.id)) {
			throw 'Client ID重复';
		}
		let client = new param.clazz(param.id, {
			onSubscribe: (topic, subject) => {
				// 根据topic存储client
				if (this.topic2clients.get(topic) == null) {
					this.topic2clients.set(topic, new Map<string, InnerMqClient>);
				}
				this.topic2clients.get(topic)?.set(client.getId(), client);
				// 完成存储后执行其它回调方法
				this.clientSubscribeCallback(client, topic, subject);
			}
		});
		this.clients.set(param.id, client);
		return client;
	}

	/* 销毁连接 */
	public destroyClient(client: InnerMqClient): void {
		// 删除客户端
		this.clients.delete(client.getId());
		// 删除根据Topic存储的客户端
		for (let topic of this.topic2clients.keys()) {
			this.topic2clients.get(topic)?.delete(client.getId());
		}
		client.destroy();
	}

	/* 发布 */
	public pub(topic: Topic, msg: any, option?: { persistent: boolean, type: PersistentType }): void {
		let published = false;
		let clients = this.topic2clients.get(topic);
		if (clients != null) {
			for (let client of clients.values()) {
				if (!client.isDestroyed()) {
					let subject = client.getSubject(topic);
					if (subject != null && !subject.closed) {
						subject.next(msg);
						published = true;
					}
				}
			}
		}
		// 消息未发送，进行持久化存储
		if (!published && (option && option.persistent)) {
			if (this.persistentQueue.get(topic) == null) {
				this.persistentQueue.set(topic, []);
			}
			this.persistentQueue.get(topic)?.push({ type: option.type, data: msg });
		}
	}

	/* 客户端订阅回调 */
	private clientSubscribeCallback(client: InnerMqClient, topic: Topic, subject: Subject<any>): void {
		// 处理持久化消息
		this.processPersistentQueue(topic, subject);
	}

	/* 处理持久化消息 */
	private processPersistentQueue(topic: Topic, subject: Subject<any>): void {
		let queue = this.persistentQueue.get(topic);
		if (queue == null) {
			return;
		}
		// 异步发送已持久化的消息
		new Observable<boolean>((observer) => {
			Promise.resolve().then(() => {
				observer.next(true);
			})
		}).subscribe(() => {
			if (queue == null) {
				return;
			}
			for (let i = 0; i < queue.length; i++) {
				switch (queue[i].type) {
					case PersistentType.ON_ONCE_SUB:
						subject.next(queue[i].data);
						queue.splice(i, 1); // 将使后面的元素依次前移，数组长度减1
						i--; // 如果不减，将漏掉一个元素
						break;
					case PersistentType.ON_EVERY_CLIENT_EVERY_SUB:
						subject.next(queue[i].data);
						break;
					default:
						break;
				}
			}
			if (queue.length == 0) {
				this.persistentQueue.delete(topic);
			}
		});
	}

}

export enum PersistentType {
	ON_ONCE_SUB, // 只进行一次缓存，一次sub后即删除
	ON_EVERY_CLIENT_EVERY_SUB, // 持久化，对每个客户端的每一次该TOPIC的sub都发送信息
}
