package com.sirma.hotel;

import com.sirma.hotel.room.Amenities;
import com.sirma.hotel.room.RoomModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HotelConfig {

    public static void main(String[] args) {
        List<Amenities> smallHotel = new ArrayList<>();
        ConcurrentHashMap<RoomModel, List<Amenities>> roomAmenitiesSmallHotel = new ConcurrentHashMap<>();
        Amenities[] amenitiesSmall = Amenities.values();
        RoomModel[] models = RoomModel.values();
        smallHotel.add(amenitiesSmall[1]);
        smallHotel.add(amenitiesSmall[0]);
        roomAmenitiesSmallHotel.put(models[0], smallHotel);
        smallHotel = new ArrayList<>();
        int counter = 1;

        // Some bullshit way I distribute amenities to different hotel types, final variation would be
        // some GUI that you click pick what amenities you offer...
        for (int i = 7; i < amenitiesSmall.length; i += 4) {
            smallHotel.add(amenitiesSmall[4]);
            smallHotel.add(amenitiesSmall[i - 2]);
            smallHotel.add(amenitiesSmall[i - 1]);
            roomAmenitiesSmallHotel.put(models[counter], smallHotel);
            counter++;
            smallHotel = new ArrayList<>();
        }
        HotelModel hotel1 = HotelModel.newInstanceOfHotel(2, 14, "Play Motel", roomAmenitiesSmallHotel, 60, 90, 120, 200);
        HotelModel hotel2 = HotelModel.newInstanceOfHotel(3, 3, "Family Hotel", roomAmenitiesSmallHotel, 40, 60, 100, 120);
        HotelModel hotel3 = HotelModel.newInstanceOfHotel(9, 99, "MGM GRAND", roomAmenitiesSmallHotel, 20, 40, 60, 80);
        // just for testing
//        HotelModel hotel1Copy = (HotelModel) HotelModel.readObject("Play Motel");
//        for (Map.Entry<Room, Integer> room : hotel1Copy.getRooms().entrySet()) {
//            System.out.println(room.getKey().toString());
//        }
//
//        System.out.println(hotel1Copy.getRoomAmenities().toString());
    }
}
