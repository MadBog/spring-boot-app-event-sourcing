package ro.bogdanlyex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.tuple.Tuple2;
import ro.bogdanlyex.configurations.EventBusConfiguration;

@Controller
public class EventBusController {

    @Autowired
    private EventBus eventBus;

    @RequestMapping("/event")
    @ResponseBody
    public DeferredResult<String> event(@RequestParam("event") String event) {

        DeferredResult<String> result = new DeferredResult<>();
        eventBus.notify(EventBusConfiguration.NEW_EVENT, Event.wrap(Tuple2.of(result, event))); // notify the event bus

        return result;
    }

}
