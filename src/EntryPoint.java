import com.sirma.booking.admin.Admin;
import com.sirma.user.ClientEntry;

public class EntryPoint {
    public static void main(String[] args) {
        // Files are generated in Resource directory
        // 1. Firstly Run HotelConfig class(From HotelConfig.java)!!! To initialize hotels from HotelModel class
        // and save to files.
        // 2. Run addCalendarYears once to initialize a calendar year and save in Book class.
        // 3. On second execution of EntryPoint comment the admin.addCalendarYears(int) method,
        // to avoid deleting booked dates in Book class.
        ClientEntry start = new ClientEntry();
        Admin.addCalendarYears(2024);
        start.start();
    }
}