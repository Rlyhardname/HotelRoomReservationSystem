package com.sirma.user;

import com.sirma.booking.Booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final String mobileNumber;
    private List<Booking> bookings;

    public User(String username, String password, String mobileNumber) {
        this.username = username;
        this.password = password;
        this.mobileNumber = mobileNumber;
        bookings = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        UserService.saveToFile(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Booking item : bookings) {
            sb.append(item);
        }
        return username + sb;
    }

    public Booking removeIndex(int index) {
        var booking = bookings.remove(index);
        if (booking != null) {
            UserService.saveToFile(this);
        }
        return booking;
    }
}
