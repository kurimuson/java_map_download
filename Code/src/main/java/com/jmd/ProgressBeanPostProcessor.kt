package com.jmd

import com.jmd.model.result.ProcessInitializationResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class ProgressBeanPostProcessor : BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private val count = AtomicInteger(0)
    private val total = 232

    @Throws(BeansException::class)
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        count.incrementAndGet()
        val result = ProcessInitializationResult()
        result.perc = count.get() * 100 / total
        result.beanName = bean.javaClass.packageName + "." + bean.javaClass.simpleName
        beans.onNext(result)
        // println(count.get())
        return bean
    }

    override fun onApplicationEvent(applicationEvent: ContextRefreshedEvent) {
        beans.onComplete()
    }

    companion object {
        private val beans: Subject<ProcessInitializationResult> = BehaviorSubject.create()

        @JvmStatic
        fun observe(): Observable<ProcessInitializationResult> {
            return beans
        }
    }

}