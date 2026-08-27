package model;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BusMessageDeserializer extends ObjectMapperDeserializer<BusMessage> {
    public BusMessageDeserializer() {
        super(BusMessage.class);
    }
}