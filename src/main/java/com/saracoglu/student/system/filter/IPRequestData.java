package com.saracoglu.student.system.filter;

public class IPRequestData {
    private long lastRequestTime;
    private long requestCount;

    public IPRequestData(long lastRequestTime, long requestCount) {
        this.lastRequestTime = lastRequestTime;
        this.requestCount = requestCount;
    }

    public long getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(long lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }
}
