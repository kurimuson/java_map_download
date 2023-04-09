import { Subject, Subscription } from "rxjs";
import { InnerMqClient } from "../inner-mq.client";
import { Topic } from "../../topic";
import { Random } from "../../util/random";

export class NormalInnerMqClient implements InnerMqClient {

	protected subjects: Map<Topic, Subject<any>> = new Map<Topic, Subject<any>>(); // 实例
	protected subscriptions: Array<{ id: string, topic: Topic, subscription: Subscription }> = []; // 订阅列表
	protected destroyed = false;

	constructor(
		protected readonly id: string,
		protected callback: {
			onSubscribe: (topic: Topic, subject: Subject<any>) => void
		}
	) {
	}

	public getId(): string {
		return this.id;
	}

	public getSubject(topic: Topic): Subject<any> | undefined {
		return this.subjects.get(topic);
	}

	public isDestroyed(): boolean {
		return this.destroyed;
	}

	/* 订阅 */
	public sub<T>(topic: Topic, subscribe: (e: T) => void): string {
		let subject = this.subjects.get(topic);
		if (subject == null) {
			subject = new Subject<any>();
			this.subjects.set(topic, subject);
		}
		let id = this.getSubscriptionId();
		let subscription = subject.subscribe(res => subscribe(res));
		this.subscriptions.push({ id: id, topic: topic, subscription: subscription });
		this.callback.onSubscribe(topic, subject);
		return id;
	}

	/* 发布 */
	public pub(topic: Topic, msg: any): void {
		console.error('仅lo-client支持内部发布');
	}

	/* 取消订阅 By Topic */
	public stopSubByTopic(topic: Topic): void {
		for (let i = 0; i < this.subscriptions.length; i++) {
			if (topic == this.subscriptions[i].topic) {
				this.subscriptions[i].subscription.unsubscribe();
				this.subscriptions.splice(i, 1);
				i--;
			}
		}
	}

	/* 取消订阅 By Id */
	public stopSubBySubscriptionId(id: string): void {
		for (let i = 0; i < this.subscriptions.length; i++) {
			if (id == this.subscriptions[i].id) {
				this.subscriptions[i].subscription.unsubscribe();
				this.subscriptions.splice(i, 1);
				i--;
			}
		}
	}

	/* 销毁 */
	public destroy(): void {
		this.destroyed = true;
		for (let i = 0; i < this.subscriptions.length; i++) {
			this.subscriptions[i].subscription.unsubscribe();
		}
		for (let subject of this.subjects.values()) {
			subject.unsubscribe();
		}
		this.subjects.clear();
	}

	private getSubscriptionId(): string {
		let id = Random.generateCharMixed(20);
		for (let i = 0; i < this.subscriptions.length; i++) {
			if (id == this.subscriptions[i].id) {
				this.getSubscriptionId();
			}
		}
		return id;
	}

}
