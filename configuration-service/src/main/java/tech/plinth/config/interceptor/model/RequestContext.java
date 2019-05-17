package tech.plinth.config.interceptor.model;

import org.springframework.stereotype.Component;

@Component
public class RequestContext {
    private String platformId;
    private String requestId;

    public RequestContext() {
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
