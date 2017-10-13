package org.korbit.iot.demoroz.services;

import org.korbit.iot.demoroz.dto.OutEvent;
import org.korbit.iot.demoroz.dto.input.MessageDto;
import org.korbit.iot.demoroz.dto.input.PowerChangeDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class MockDevice {

    RestTemplate  restTemplate = new RestTemplate();
    final  private String API_URL = "http://localhost:8080/send_data/";
    public void getEvent(OutEvent event) {
        if (event.getType().equals("state"))  getPowerEvent(event);

    }
    String getPowerEvent (OutEvent event) {
        System.out.println("Пришел эвент на включение/выключение");
        pushData("power",new PowerChangeDto(UUID.fromString(event.getUuid()),event.getSource().get("state").equals("true")));
        return "Success";
     }
     void pushData(String type,MessageDto event) {
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity<?> entity = new HttpEntity<>(event,headers);
         restTemplate.postForObject(API_URL+type,entity,String.class);
     }
}
