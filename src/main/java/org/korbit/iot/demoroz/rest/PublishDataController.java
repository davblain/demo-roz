package org.korbit.iot.demoroz.rest;


import org.korbit.iot.demoroz.dto.input.PowerChangeDto;
import org.korbit.iot.demoroz.dto.input.TemperatureChangeDto;
import org.korbit.iot.demoroz.events.PowerChangeEvent;
import org.korbit.iot.demoroz.events.TemperatureChangeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("send_data")
public class PublishDataController  {
    private final ApplicationEventPublisher publisher;
    public PublishDataController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    @RequestMapping(value = "temperature", method = RequestMethod.POST)
    String publishData(@RequestBody TemperatureChangeDto tepData) {
     publisher.publishEvent(new TemperatureChangeEvent(this,tepData.getUuid(),tepData.getTemperature()));
     return "SUCCESS";
    }
    @RequestMapping(value = "power", method = RequestMethod.POST)
    String publishState( @RequestBody PowerChangeDto state) {
        publisher.publishEvent(new PowerChangeEvent(this, state.getUuid(),state.getState()));
        return "SUCCESS";
    }
}
