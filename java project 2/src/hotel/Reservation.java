package hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private final String reservationId;
    private final String guestName;
    private final int roomNumber;
    private final RoomCategory category;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalAmount;
    private ReservationStatus status;

    public Reservation(String reservationId, String guestName, int roomNumber, RoomCategory category,
                       LocalDate checkIn, LocalDate checkOut, double totalAmount, ReservationStatus status) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void cancel() {
        status = ReservationStatus.CANCELLED;
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public boolean overlaps(LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
        return status == ReservationStatus.CONFIRMED
                && checkIn.isBefore(requestedCheckOut)
                && requestedCheckIn.isBefore(checkOut);
    }

    public String toCsv() {
        return String.join(",",
                reservationId,
                escape(guestName),
                String.valueOf(roomNumber),
                category.name(),
                checkIn.toString(),
                checkOut.toString(),
                String.format("%.2f", totalAmount),
                status.name()
        );
    }

    public static Reservation fromCsv(String line) {
        String[] parts = line.split(",", -1);
        return new Reservation(
                parts[0],
                unescape(parts[1]),
                Integer.parseInt(parts[2]),
                RoomCategory.valueOf(parts[3]),
                LocalDate.parse(parts[4]),
                LocalDate.parse(parts[5]),
                Double.parseDouble(parts[6]),
                ReservationStatus.valueOf(parts[7])
        );
    }

    private static String escape(String value) {
        return value.replace("%", "%25").replace(",", "%2C");
    }

    private static String unescape(String value) {
        return value.replace("%2C", ",").replace("%25", "%");
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + System.lineSeparator()
                + "Guest: " + guestName + System.lineSeparator()
                + "Room: " + roomNumber + " (" + category.getDisplayName() + ")" + System.lineSeparator()
                + "Stay: " + checkIn + " to " + checkOut + " (" + getNights() + " night(s))" + System.lineSeparator()
                + "Payment: $" + String.format("%.2f", totalAmount) + " PAID" + System.lineSeparator()
                + "Status: " + status;
    }
}
