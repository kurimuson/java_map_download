import { Subject } from "rxjs";
import { Topic } from "../topic";
import { PersistentType } from "../service/inner-mq.service";

export interface InnerMqClient {

	getId(): string;

	getSubject(topic: Topic): Subject<any> | undefined;

	isDestroyed(): boolean;

	sub<T>(topic: Topic, subscribe: (e: T) => void): string;

	pub(topic: Topic, msg: any, option?: { persistent: boolean, type: PersistentType }): void;

	stopSubByTopic(topic: Topic): void;

	stopSubBySubscriptionId(id: string): void;

	destroy(): void;

}
