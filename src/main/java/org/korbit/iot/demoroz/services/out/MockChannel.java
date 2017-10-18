package org.korbit.iot.demoroz.services.out;

import org.apache.log4j.Logger;
import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.services.MockDevice;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.springframework.stereotype.Component;

@Component("mocOutboundChannel")
public class MockChannel implements OutboundChannel {
    final private Logger logger = Logger.getLogger(MockChannel.class);
    final private MockDevice device;
    public MockChannel(MockDevice device) {
        this.device = device;
    }


    @Override
    public void pushEvent(OutEvent event) {
        this.device.getEvent(event);
    }
}
