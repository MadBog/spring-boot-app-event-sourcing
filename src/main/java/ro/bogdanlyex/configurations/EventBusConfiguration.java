package ro.bogdanlyex.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.Environment;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.core.dispatch.ThreadPoolExecutorDispatcher;
import reactor.fn.tuple.Tuple2;
import reactor.spring.context.config.EnableReactor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.pow;
import static reactor.Environment.initializeIfEmpty;
import static reactor.bus.selector.Selectors.$;

@EnableReactor
@Configuration
public class EventBusConfiguration {

    public static final String NEW_EVENT = "NEW_EVENT";


    @Bean
    public Environment reactorEnv() {
        return initializeIfEmpty()
                .assignErrorJournal();
    }

    @Bean
    public ExecutorService cachedThreadPool() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public ThreadPoolExecutorDispatcher threadPoolExecutorDispatcher() {
        return new ThreadPoolExecutorDispatcher((int) pow(2, 10), (int) pow(2, 10), cachedThreadPool());
    }

    @Bean
    public EventBus eventBus() {
        EventBus eventBus = EventBus.create(reactorEnv(), threadPoolExecutorDispatcher());

        // setup event channels
        eventBus.on($(NEW_EVENT), (Event<Tuple2<DeferredResult<String>, String>> event) -> {
            System.out.println(Thread.currentThread() + ": " + event.getData().getT2());
            event.getData().getT1().setResult(event.getData().getT2()); // set the deferred result so the user is responded to
        });

        return eventBus;
    }

}
