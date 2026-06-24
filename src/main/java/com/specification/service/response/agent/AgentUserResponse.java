package com.specification.service.response.agent;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
public class AgentUserResponse {
    
   private String response;
   private String modifiedDetail;

    public AgentUserResponse(String response) {
        this.response = response;
    }

    public AgentUserResponse(String response,String modifiedDetail) {
        this.modifiedDetail = modifiedDetail;
        this.response = response;
    }


}
