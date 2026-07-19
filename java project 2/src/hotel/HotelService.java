package hotel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HotelService {
    private final FileStorage storage;
    private final PaymentProcessor paymentProcessor;
    private final List<Room> rooms;
    private final List<Reservation> reservations;

    public HotelService(FileStorage storage, PaymentProcessor paymentProcessor) throws IOException {
        this.storage = storage;
        this.paymentProcessor = paymentProcessor;
        rooms = storage.loadRooms();
        reservations = storage.loadReservations();
    }

    public List<Room> searchAvailableRooms(RoomCategory category, LocalDate checkIn, LocalDate checkOut) {
        validateDates(checkIn, checkOut);
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCategory() == category && isRoomAvailable(room.getRoomNumber(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Reservation bookRoom(String guestName, RoomCategory category, LocalDate checkIn, LocalDate checkOut)
            throws IOException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name is required.");
        }

        List<Room> availableRooms = searchAvailableRooms(category, checkIn, checkOut);
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("No available " + category.getDisplayName() + " rooms for those dates.");
        }

        Room selectedRoom = availableRooms.get(0);
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalAmount = nights * selectedRoom.getCategory().getNightlyRate();

        if (!paymentProcessor.processPayment(guestName.trim(), totalAmount)) {
            throw new IllegalStateException("Payment failed. Booking was not created.");
        }

        Reservation reservation = new Reservation(
                "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                guestName.trim(),
                selectedRoom.getRoomNumber(),
                selectedRoom.getCategory(),
                checkIn,
                checkOut,
                totalAmount,
                ReservationStatus.CONFIRMED
        );
        reservations.add(reservation);
        storage.saveReservations(reservations);
        return reservation;
    }

    public boolean cancelReservation(String reservationId) throws IOException {
        Optional<Reservation> reservation = findReservation(reservationId);
        if (reservation.isPresent() && reservation.get().getStatus() == ReservationStatus.CONFIRMED) {
            reservation.get().cancel();
            storage.saveReservations(reservations);
            return true;
        }
        return false;
    }

    public Optional<Reservation> findReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equalsIgnoreCase(reservationId)) {
                return Optional.of(reservation);
            }
        }
        return Optional.empty();
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }

    private boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoomNumber() == roomNumber && reservation.overlaps(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required.");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
    }
}
