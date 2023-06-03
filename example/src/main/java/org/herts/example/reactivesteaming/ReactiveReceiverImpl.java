package org.herts.example.reactivesteaming;

public class ReactiveReceiverImpl implements ReactiveReceiver {

    @Override
    public void onReceived(String value) {
        System.out.println("Received data on client receiver: " + value);
    }
}
