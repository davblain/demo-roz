package org.korbit.iot.demoroz.services.out;

import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.services.interfaces.OutboundChannel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class HttpOutboundChannel implements OutboundChannel {
    final private String API_PATH  = "http://localhost:8090/publish_data";
    private final RestTemplate restTemplate = new RestTemplate();
    public  void   pushEvent (OutEvent event) {
        HttpEntity<?> entity = new HttpEntity<>(event.getSource());
        restTemplate.exchange(API_PATH+event.getType(), HttpMethod.POST, entity,String.class);
    }

}
