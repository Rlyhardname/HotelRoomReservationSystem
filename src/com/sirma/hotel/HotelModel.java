package com.sirma.hotel;

import com.sirma.booking.Booking;
import com.sirma.hotel.room.Amenities;
import com.sirma.hotel.room.Room;
import com.sirma.hotel.room.RoomModel;
import com.sirma.hotel.room.RoomStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotelModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905144703452808602L;
    private String name;

    private Map<Room, Integer> rooms;

    private Map<RoomModel, List<Room>> roomsByModel;

    private Map<RoomModel, Double> prices;

    private ConcurrentHashMap<RoomModel, List<Amenities>> roomAmenities;

    private int lastReservationID;

    private ConcurrentHashMap<Integer, Booking> bookings;

    HotelModel(int floors, int roomsPerFloor, String name, ConcurrentHashMap<RoomModel, List<Amenities>> roomAmenities, double... perNight) {
        rooms = new HashMap<>();
        prices = new HashMap<>();
        roomsByModel = new HashMap<>();
        bookings = new ConcurrentHashMap<>();
        this.name = name;
        this.roomAmenities = roomAmenities;
        this.prices = new HashMap<>();
        prices.put(RoomModel.SINGLE, perNight[0]);
        prices.put(RoomModel.DOUBLE, perNight[1]);
        prices.put(RoomModel.DELUXE, perNight[2]);
        prices.put(RoomModel.SUITE, perNight[3]);
        lastReservationID = 0;
        initRooms(floors, roomsPerFloor);
    }

    public void book(int id, Booking userBooking){
        bookings.put(id,userBooking);
        saveObjectToFile(this.getName());
    }

    public static HotelModel newInstanceOfHotel(int floors, int roomsPerFloor, String name, ConcurrentHashMap<RoomModel, List<Amenities>> roomAmenities, double... perNight) {
        return new HotelModel(floors, roomsPerFloor, name, roomAmenities, perNight);
    }

    public int getNewReservationId(){
        bookings.put(++lastReservationID,new Booking());
        return lastReservationID;
    }

    private void initRooms(int floors, int roomsPerFloor) {

        for (int i = 0; i <= floors; i++) {
            for (int j = 0; j < roomsPerFloor; j++) {
                Room room = buildRoom(i, j);
                rooms.put(room, 1);
            }

        }
        List<Room> singleRoom = new ArrayList<>();
        List<Room> doubleRoom = new ArrayList<>();
        List<Room> deluxe = new ArrayList<>();
        List<Room> suite = new ArrayList<>();
        for (Map.Entry<Room, Integer> currentRoom : rooms.entrySet()) {
            Room curRoom = currentRoom.getKey();
            switch (curRoom.getModel()) {

                case SINGLE -> singleRoom.add(curRoom);

                case DOUBLE -> doubleRoom.add(curRoom);

                case DELUXE -> deluxe.add(curRoom);

                case SUITE -> suite.add(curRoom);
            }

        }
        roomsByModel.put(RoomModel.SINGLE, new ArrayList<>(singleRoom));
        roomsByModel.put(RoomModel.DOUBLE, new ArrayList<>(doubleRoom));
        roomsByModel.put(RoomModel.DELUXE, new ArrayList<>(deluxe));
        roomsByModel.put(RoomModel.SUITE, new ArrayList<>(suite));
        saveObjectToFile(this.getName());
    }

    private Room buildRoom(int indexI, int indexJ) {
        int roomNumber;
        if (indexJ < 10) {
            roomNumber = Integer.parseInt((indexI + "0" + indexJ));
        } else {
            roomNumber = Integer.parseInt((indexI + "" + indexJ));
        }

        int module = indexJ % 10;
        RoomModel model = null;
        if (module < 4) {
            model = RoomModel.SINGLE;
        } else if (module < 7 && module > 3) {
            model = RoomModel.DOUBLE;
        } else if (module < 9 && module > 6) {
            model = RoomModel.DELUXE;
        } else if (module == 9) {
            model = RoomModel.SUITE;
        }

        Room room = switch (model) {

            case SINGLE -> new Room(roomNumber,
                    prices.get(RoomModel.SINGLE),
                    model
            );

            case DOUBLE -> new Room(roomNumber,
                    prices.get(RoomModel.DOUBLE),
                    model
            );

            case DELUXE -> new Room(roomNumber,
                    prices.get(RoomModel.DELUXE),
                    model
            );

            case SUITE -> new Room(roomNumber,
                    prices.get(RoomModel.SUITE),
                    model
            );


        };

        return room;
    }


    public void saveObjectToFile(String name) {
        Path path = Path.of("resources\\hotels\\"+name+".txt");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toString()))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Decided to not simulate a noSQL db, but to use Serializable
//    public void saveToFile(HashMap<Room, Integer> rooms, String hotelName) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<Room, Integer> set :
//                rooms.entrySet()) {
//            sb.append("Id:" + set.getKey().getRoomNumber() + " ");
//            sb.append("pricePerNight:" + set.getKey().getPricePerNight() + " ");
//            sb.append("model:" + set.getKey().getModel() + " ");
//            sb.append(System.lineSeparator());
//        }
//
//
//        Path path = Path.of("D:\\BookingSystem\\hotels\\" + hotelName + ".txt");
//        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//            writer.write(sb.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static HotelModel readObject(String name) {
        Path path = Path.of("resources\\hotels\\"+name+".txt");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
            return (HotelModel)ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public String getName() {
        return name;
    }

    public Map<Room, Integer> getRooms() {
        return rooms;
    }

    public ConcurrentHashMap<RoomModel, List<Amenities>> getRoomAmenities() {
        return roomAmenities;
    }

    public ConcurrentHashMap<Integer, RoomStatus> getRoomsMap() {
        ConcurrentHashMap<Integer, RoomStatus> mapIds = new ConcurrentHashMap<>();
        for (Map.Entry<Room, Integer> room : rooms.entrySet()) {
            mapIds.put(room.getKey().getRoomNumber(), RoomStatus.AVAILABLE);
        }
        return mapIds;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public List<Room> getRoomsByModel(RoomModel model) {
        return roomsByModel.get(model);
    }


    public Map<RoomModel, Double> getPrices() {
        return prices;
    }


}
