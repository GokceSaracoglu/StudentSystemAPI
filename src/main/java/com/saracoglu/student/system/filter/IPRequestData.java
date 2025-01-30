package com.saracoglu.student.system.filter;

public class IPRequestData {
    private long lastRequestTime; // Son istek zamanı
    private long requestCount;    // İstek sayısı

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
