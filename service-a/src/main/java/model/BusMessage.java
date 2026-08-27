package model;

public record BusMessage(
        String cid,
        String capability,
        boolean needReply,
        Object payload
) {}