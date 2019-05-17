package tech.plinth.config.interceptor.model;

import org.springframework.stereotype.Component;

@Component
public class RequestContext {
    private String platformId;

    public RequestContext() {
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

}
