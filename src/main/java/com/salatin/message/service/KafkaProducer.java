package com.salatin.message.service;

public interface KafkaProducer<T> {

    void send(String objectId, T message);
}
