package com.sirma.hotel.room;

public enum RoomStatus {
    AVAILABLE(1),BOOKED(2),SELECTED(3),UNDER_RENOVATION(404);

    public final int status;
    RoomStatus(int status) {
        this.status = status;
    }
}
