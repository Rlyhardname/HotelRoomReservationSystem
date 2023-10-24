package com.sirma.user;

import com.sirma.booking.Book;
import com.sirma.booking.Booking;
import com.sirma.booking.BookingService;
import com.sirma.hotel.HotelModel;
import com.sirma.hotel.room.Room;
import com.sirma.hotel.room.RoomModel;
import com.sirma.hotel.room.RoomStatus;
import com.sirma.user.Login;
import com.sirma.user.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientEntry {
    private BookingService booking;
    private Login login;
    private User user;
    private Scanner sc;

    public ClientEntry() {
        login = new Login();
        booking = new BookingService();
    }

    // Admin command, but there's no admin

    public void start() {
        try {
            sc = new Scanner(System.in);
            login();
            runProgram();
        } catch (RuntimeException e) {
            System.out.println("Scanner error");
            e.printStackTrace();
        } finally {
            sc.close();
        }

    }

    private boolean login() {
        while (true) {
            System.out.println("Type S or L for signUp/login or 'x' to exit the program");
            String action = sc.nextLine();
            if (action.toLowerCase().equals("l")) {
                System.out.println("Type 'username,pass' with the quote marks for login");
                String[] actions = sc.nextLine().split(",");
                user = login.login(actions[0], actions[1]);
                return true;
            } else if (action.toLowerCase().equals("s")) {
                System.out.println("Type 'username,pass,phoneNumber' with the quote marks for signup" + System.lineSeparator());
                String[] actions = sc.nextLine().split(",");
                login.signUp(actions[0], actions[1], actions[2]);
            } else if (action.toLowerCase().equals("x")) {
                System.exit(0);
            }

        }

    }

    private boolean runProgram() {
        while (true) {
            System.out.printf("Type the digit for:%n'1'.View hotels and rooms%n'2'.Cancel Booking%n'3'.Exit program%n");
            int action = Integer.parseInt(sc.nextLine());
            switch (action) {
                case 1:
                    viewHotelsAndRooms();
                    break;
                case 2:
                    cancelBooking();
                    break;
                case 3:
                    sc.close();
                    System.exit(0);

            }

        }

    }

    private void viewHotelsAndRooms() {
        int[] yearMonthDay = pickDate();
        if (booking.isAvailable(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2])) {
            System.out.println("Pick hotel and room type in format hotelName,roomType " + System.lineSeparator());
            String[] hotelAndRoom = sc.nextLine().split(",");
            //TEST CASE:
            //String[] hotelAndRoom = {"hotel", "single"};
            RoomModel model = switch (hotelAndRoom[1].toLowerCase()) {
                case "single" -> RoomModel.SINGLE;
                case "double" -> RoomModel.DOUBLE;
                case "deluxe" -> RoomModel.DELUXE;
                case "suite" -> RoomModel.SUITE;
                default -> throw new IllegalStateException("Unexpected value: " + hotelAndRoom[1]);
            };

            Room room = booking.checkAvailability(yearMonthDay, hotelAndRoom[0], model);
            if (room != null) {
                isBookingFinalized(yearMonthDay, hotelAndRoom, room);
            }

        }
    }

    public void cancelBooking() {
        List<Booking> bookings = user.getBookings();
        for (int i = 0; i < bookings.size(); i++) {
            var item = bookings.get(i);
            System.out.println(item + "%n index:" + i + System.lineSeparator());
        }

        while (true) {
            System.out.println("Type index of booking you wish to remove or 'b' to go back to the menu, or 'x' to exit application" + System.lineSeparator());
            String id = sc.nextLine();
            if (id.toLowerCase().equals("b")) {
                break;
            } else if (id.toLowerCase().equals("x")) {
                sc.close();
                System.exit(0);
            }

            Booking removedBooking = user.removeIndex(Integer.parseInt(id));
            if (removedBooking == null) {
                System.out.println("You don't have booking with id of " + id + System.lineSeparator());
                continue;
            }

            booking.getBook().changeRoomStatus(removedBooking.getDate(), removedBooking.getHotelName(), removedBooking.getRoomId(), RoomStatus.AVAILABLE);
            booking.saveToFile();
            HotelModel.readObject(removedBooking.getHotelName()).book(removedBooking.getReservationId(), new Booking());
            break;

        }

    }

    private int[] pickDate() {
        System.out.println("input dates in format year,month,day" + System.lineSeparator());
        //TEST CASE:
        //int[] yearMonthDay = {2024, 01, 10};
        return Arrays.stream(sc.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
    }

    private boolean isBookingFinalized(int[] yearMonthDay, String[] hotelAndRoom, Room room) {
        System.out.println("Room is available, do you want to finalize the booking? 'Y' OR 'N'" + System.lineSeparator());
        String decision = sc.nextLine();
        if (decision.toLowerCase().equals("y")) {
            Book book = booking.getBook();
            System.out.println("Status before change: " + book.getRoomStatus(LocalDate.of(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]),
                    hotelAndRoom[0],
                    room)
                    + System.lineSeparator()
            );

            System.out.println("Status after change " + book.changeRoomStatus(LocalDate.of(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]),
                    hotelAndRoom[0],
                    room.getRoomNumber(),
                    RoomStatus.BOOKED)
                    + System.lineSeparator());
            HotelModel hotel = HotelModel.readObject(hotelAndRoom[0]);
            int reservationId = hotel.getNewReservationId();
            LocalDate date = LocalDate.of(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]);
            Booking userBooking = new Booking(reservationId, user.getUsername(), hotel.getName(), date, room.getRoomNumber());
            hotel.book(reservationId, userBooking);
            user.addBooking(userBooking);
            return true;

        }

        return false;
    }

}
