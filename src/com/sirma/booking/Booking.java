package com.sirma.booking;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {

    private int reservationId;
    private String userName;
    private String hotelName;
    private LocalDate date;
    private int roomId;

    public Booking(int reservationId, String userId, String hotelName, LocalDate date, int roomId) {
        this.reservationId = reservationId;
        this.userName = userId;
        this.hotelName = hotelName;
        this.date = date;
        this.roomId = roomId;
    }

    public Booking(){

    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public String toString(){

        return "Hotel:"  + this.hotelName + "%n reservationId:" + this.reservationId + "%n date:" + this.date + "%n roomId" + roomId;
    }
}
