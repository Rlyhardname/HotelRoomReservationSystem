package com.sirma.hotel.room;

public enum RoomModel {
    SINGLE(1),DOUBLE(2),DELUXE(4),SUITE(6);

    public final int maxOccupancy;
    RoomModel(int max) {
        maxOccupancy = max;
    }
}
