package com.sirma.booking;

import com.sirma.hotel.room.Room;
import com.sirma.hotel.room.RoomStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = -162080109678617647L;
    private static Book book;
    private ConcurrentHashMap<LocalDate, ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>>>
            locations;

    private Book() {
        locations = new ConcurrentHashMap<>();
    }

    public static Book instanceOf() {
        if (book == null) {
            book = new Book();
        }

        return book;
    }

    public RoomStatus changeRoomStatus(LocalDate date, String hotelName, Integer roomId, RoomStatus status) {
        locations.get(date).get(hotelName).put(roomId, status);
        return locations.get(date).get(hotelName).get(roomId);
    }

    public RoomStatus getRoomStatus(LocalDate date, String hotelName, Room room) {
        return locations.get(date).get(hotelName).get(room.getRoomNumber());
    }

    public ConcurrentHashMap<LocalDate, ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>>> getLocations() {
        return locations;
    }

    public void setLocations(ConcurrentHashMap<LocalDate, ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>>> locations) {
        this.locations = locations;
    }
}
