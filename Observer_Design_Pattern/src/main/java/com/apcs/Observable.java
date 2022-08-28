package com.apcs;

/*Description : This file contains two interface necessary for the observer design pattern.*/

/*Description : Observable interface enforce the implementations to implement a method to register observer. If
 * thread A wants to register as an observer to thread B, the thread has to specify a valid event that will be
 * emitted by thread A.
 * */
public interface Observable {
    public void addObserver(String event, Observer observer);
}

/*Description : Observer interface enforce the implementations to implement a method to allow events to be sent. If
 * thread A wants to notify thread B that an event concerned has occurred, thread A can invoke the sendEvent function
 * with the response of the event occurred.
 * */
interface Observer {
    public void sendEvent(Response res);
}
