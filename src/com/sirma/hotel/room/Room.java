package com.sirma.hotel.room;

import java.io.Serializable;

public class Room implements Serializable {

    private final int roomNumber;
    private final double pricePerNight;
    private RoomModel model;

    public Room(int roomNumber,
                double pricePerNight,
                RoomModel model) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.model = model;
    }

    public double calcCancellationFee() {
        return pricePerNight * 0.1;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public RoomModel getModel() {
        return model;
    }

    @Override
    public String toString(){
        return "room number:" + roomNumber+ " price per night:" + pricePerNight + " room model:" + model.toString() + " CancellationFee fee:" + calcCancellationFee() + "lv.";
    }
}
