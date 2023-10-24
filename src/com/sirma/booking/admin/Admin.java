package com.sirma.booking.admin;

import com.sirma.booking.BookingService;

public class Admin {
    public static void addCalendarYears(int year) {
        BookingService service = new BookingService(); // - create calendar year in Book class
        service.addYear(year);

    }
}
