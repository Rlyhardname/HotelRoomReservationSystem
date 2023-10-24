package com.sirma.hotel.room;

public enum Amenities {

    SINGLE_BED(0),
    SHOWER(1),
    TV(2),
    CLOSET(3),
    DOUBLE_BED(10),
    AC(11),
    KETTLE_POT(12),
    IRON(13),
    HAIR_DRIER(14),
    MINI_FRIDGE(20),
    BATH_TUB(21),
    SOFA(22),
    BALCONY(23),
    ROOM_SERVICE(30),
    AIRPORT_TRANSPORT(31),
    PARKING(32);

    public final int value;


    Amenities(int value) {
        this.value = value;
    }
}
