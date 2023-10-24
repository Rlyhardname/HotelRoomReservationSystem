package com.sirma.booking;

import com.sirma.hotel.HotelModel;
import com.sirma.hotel.room.Room;
import com.sirma.hotel.room.RoomModel;
import com.sirma.hotel.room.RoomStatus;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {
    private Book book;

    public BookingService() {
        book = Book.instanceOf();
        readFromFile();
    }

    public boolean isAvailable(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        var desireDate = book.getLocations().get(date);
        System.out.println(desireDate.toString());
        if (desireDate == null) {
            return false;
        }

        for (HotelModel hotel : loadHotels()
        ) {
            var prices = hotel.getPrices();
            System.out.printf("Hotel name %s%n" +
                            "Hotel amenities %s%n" +
                            "Single Room ppn %.2f%n" +
                            "Double Room ppn %.2f%n" +
                            "Deluxe ppn %.2f%n" +
                            "Suite ppn %.2f%n", hotel.getName(),
                    hotel.getRoomAmenities().toString(),
                    prices.get(RoomModel.SINGLE),
                    prices.get(RoomModel.DOUBLE),
                    prices.get(RoomModel.DELUXE),
                    prices.get(RoomModel.SUITE)
            );
        }
        System.out.println();
        return true;
    }


    public Room checkAvailability(int[] date, String hotelName, RoomModel type) {
        HotelModel hotel = loadHotel(hotelName);
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel name " + hotelName + " is wrong!");
        }
        List<Room> rooms = hotel.getRoomsByModel(type);
        if (rooms == null) {
            throw new IllegalArgumentException("Room type " + type + " doesn't exist!");
        }

        var dayToCheck = book.getLocations().get(LocalDate.of(date[0], date[1], date[2])).get(hotelName);
        if (dayToCheck == null) {
            throw new IllegalArgumentException("Date unavailable!");
        }

        for (Room curRoom : rooms) {
            if (type == curRoom.getModel()) {
                if (dayToCheck.get(curRoom.getRoomNumber()) == RoomStatus.AVAILABLE) {
                    dayToCheck.put(curRoom.getRoomNumber(), RoomStatus.SELECTED);
                    System.out.println(curRoom);
                    return curRoom;
                }

            }

        }

        throw new RuntimeException("All hotel rooms of type " + type + " are already booked or under renovations," +
                "please try a different room type or Hotel");
    }

    public void saveToFile() {
        Path path = Path.of("resources\\bookings\\book.txt");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toString()))) {
            oos.writeObject(book);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void readFromFile() {
        Path path = Path.of("resources\\bookings\\book.txt");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
            book.setLocations(((Book) ois.readObject()).getLocations());

        } catch (FileNotFoundException e) {
            System.out.println("Няма инициализирани дати.. Моля попълнете поне една дата");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<HotelModel> loadHotels() {
        File file = new File("resources\\hotels\\");
        File[] files = null;
        if (file.isDirectory()) {
            files = file.listFiles();
        }

        List<HotelModel> listHotels = new ArrayList<>();
        for (File item : files) {
            HotelModel hotel;
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(item));
                hotel = (HotelModel) ois.readObject();
                listHotels.add(hotel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        return listHotels;
    }

    public HotelModel loadHotel(String hotelName) {
        File file = new File("resources\\bookings\\" + hotelName + ".txt");
        if (file.isFile()) {
            HotelModel hotel;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                hotel = (HotelModel) ois.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            return hotel;
        }

        return null;
    }


    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>> hotelsOnDate() {
        List<HotelModel> hotels = loadHotels();
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>> hotelsOnDate = new ConcurrentHashMap<>();
        for (int i = 0; i < hotels.size(); i++) {
            HotelModel hotel = hotels.get(i);
            ConcurrentHashMap<Integer, RoomStatus> rooms = hotel.getRoomsMap();
            hotelsOnDate.put(hotel.getName(), new ConcurrentHashMap<>(rooms));
        }

        return hotelsOnDate;
    }

    public void addYear(int year) {
        for (int i = 1; i <= 12; i++) {
            addMonth(year, i);
        }

    }

    protected void addMonth(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (numberOfDays > 29) {
            numberOfDays = 28;
        }

        ConcurrentHashMap<String, ConcurrentHashMap<Integer, RoomStatus>> hotelsOnDate = hotelsOnDate();
        for (int i = 1; i <= numberOfDays; i++) {
            book.getLocations().put(LocalDate.of(year, month, i), new ConcurrentHashMap<>(hotelsOnDate));
        }

        saveToFile();
    }

    public Book getBook() {
        return book;
    }
}
