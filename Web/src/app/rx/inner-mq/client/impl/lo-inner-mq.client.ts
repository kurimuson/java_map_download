import { NormalInnerMqClient } from "./normal-inner-mq.client";
import { Topic } from "../../topic";
import { Observable, Subject } from "rxjs";
import { PersistentType } from "../../service/inner-mq.service";

/**
 * 支持本地回环模式的客户端
 * 即自己可以给自己发消息，不需要通过服务端接收
 * 类似于 127.0.0.1
 * */
export class LoInnerMqClient extends NormalInnerMqClient {

	// 持久化队列，仅本地回环支持
	private persistentQueue = new Map<any, Array<{ type: PersistentType, data: any }>>();

	constructor(
		id: string,
		callback: {
			onSubscribe: (topic: Topic, subject: Subject<any>) => void
		}
	) {
		super(id, callback);
	}

	/* 订阅 */
	public override sub<T>(topic: Topic, subscribe: (e: T) => void): string {
		let res = super.sub<T>(topic, subscribe);
		// 完成父类sub方法后发送持久化消息
		let subject = this.subjects.get(topic);
		if (subject != null) {
			this.processPersistentQueue(topic, subject);
		}
		return res;
	}

	/* 发布 */
	public override pub(topic: Topic, msg: any, option?: { persistent: boolean, type: PersistentType }): void {
		let published = false;
		if (!this.destroyed) {
			let subject = this.subjects.get(topic);
			if (subject != null && !subject.closed) {
				subject.next(msg);
				published = true;
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
