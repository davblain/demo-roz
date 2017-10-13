package org.korbit.iot.demoroz.services.interfaces;

import org.korbit.iot.demoroz.dto.OutEvent;

public interface OutboundChannel {
    void pushEvent(OutEvent event);

}
