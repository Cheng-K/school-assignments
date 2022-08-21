package com.apcs;

public interface Observable {
    public void addObserver(String event, Observer observer);
}

interface Observer {
    public void sendEvent(Response res);
}
