package hotel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileStorage {
    private final Path dataDirectory;
    private final Path roomsFile;
    private final Path reservationsFile;

    public FileStorage(String directoryName) {
        dataDirectory = Path.of(directoryName);
        roomsFile = dataDirectory.resolve("rooms.csv");
        reservationsFile = dataDirectory.resolve("reservations.csv");
    }

    public List<Room> loadRooms() throws IOException {
        ensureDataFiles();
        List<Room> rooms = new ArrayList<>();
        for (String line : Files.readAllLines(roomsFile)) {
            if (!line.trim().isEmpty()) {
                rooms.add(Room.fromCsv(line));
            }
        }
        return rooms;
    }

    public List<Reservation> loadReservations() throws IOException {
        ensureDataFiles();
        List<Reservation> reservations = new ArrayList<>();
        for (String line : Files.readAllLines(reservationsFile)) {
            if (!line.trim().isEmpty()) {
                reservations.add(Reservation.fromCsv(line));
            }
        }
        return reservations;
    }

    public void saveReservations(List<Reservation> reservations) throws IOException {
        ensureDataFiles();
        List<String> lines = new ArrayList<>();
        for (Reservation reservation : reservations) {
            lines.add(reservation.toCsv());
        }
        Files.write(reservationsFile, lines);
    }

    private void ensureDataFiles() throws IOException {
        if (!Files.exists(dataDirectory)) {
            Files.createDirectories(dataDirectory);
        }
        if (!Files.exists(roomsFile)) {
            List<String> sampleRooms = Arrays.asList(
                    new Room(101, RoomCategory.STANDARD, 2).toCsv(),
                    new Room(102, RoomCategory.STANDARD, 2).toCsv(),
                    new Room(201, RoomCategory.DELUXE, 3).toCsv(),
                    new Room(202, RoomCategory.DELUXE, 4).toCsv(),
                    new Room(301, RoomCategory.SUITE, 4).toCsv(),
                    new Room(302, RoomCategory.SUITE, 5).toCsv()
            );
            Files.write(roomsFile, sampleRooms);
        }
        if (!Files.exists(reservationsFile)) {
            Files.createFile(reservationsFile);
        }
    }
}
