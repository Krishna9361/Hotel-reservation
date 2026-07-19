package hotel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final HotelService hotelService;

    public Main(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    public static void main(String[] args) {
        try {
            FileStorage storage = new FileStorage("data");
            HotelService service = new HotelService(storage, new PaymentProcessor());
            new Main(service).run();
        } catch (IOException exception) {
            System.out.println("Could not start the system: " + exception.getMessage());
        }
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            System.out.println();

            try {
                switch (choice) {
                    case 1:
                        searchRooms();
                        break;
                    case 2:
                        makeReservation();
                        break;
                    case 3:
                        cancelReservation();
                        break;
                    case 4:
                        viewBookingDetails();
                        break;
                    case 5:
                        listReservations();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using the Hotel Reservation System.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException | IllegalStateException | IOException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("=== Hotel Reservation System ===");
        System.out.println("1. Search available rooms");
        System.out.println("2. Make reservation");
        System.out.println("3. Cancel reservation");
        System.out.println("4. View booking details");
        System.out.println("5. View all reservations");
        System.out.println("0. Exit");
    }

    private void searchRooms() {
        RoomCategory category = readCategory();
        LocalDate checkIn = readDate("Check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Check-out date (YYYY-MM-DD): ");
        List<Room> rooms = hotelService.searchAvailableRooms(category, checkIn, checkOut);

        if (rooms.isEmpty()) {
            System.out.println("No rooms are available for your search.");
            return;
        }

        System.out.println("Available rooms:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }

    private void makeReservation() throws IOException {
        System.out.print("Guest name: ");
        String guestName = scanner.nextLine();
        RoomCategory category = readCategory();
        LocalDate checkIn = readDate("Check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = readDate("Check-out date (YYYY-MM-DD): ");

        Reservation reservation = hotelService.bookRoom(guestName, category, checkIn, checkOut);
        System.out.println("Reservation confirmed!");
        System.out.println(reservation);
    }

    private void cancelReservation() throws IOException {
        System.out.print("Reservation ID: ");
        String reservationId = scanner.nextLine();
        if (hotelService.cancelReservation(reservationId)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Reservation not found or already cancelled.");
        }
    }

    private void viewBookingDetails() {
        System.out.print("Reservation ID: ");
        String reservationId = scanner.nextLine();
        Optional<Reservation> reservation = hotelService.findReservation(reservationId);
        if (reservation.isPresent()) {
            System.out.println(reservation.get());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private void listReservations() {
        List<Reservation> reservations = hotelService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations stored yet.");
            return;
        }

        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("------------------------------");
        }
    }

    private RoomCategory readCategory() {
        System.out.println("Room category:");
        System.out.println("1. Standard");
        System.out.println("2. Deluxe");
        System.out.println("3. Suite");
        return RoomCategory.fromMenuChoice(readInt("Choose category: "));
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException exception) {
                System.out.println("Please enter a valid date in YYYY-MM-DD format.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
