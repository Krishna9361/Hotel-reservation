package hotel;

public class Room {
    private final int roomNumber;
    private final RoomCategory category;
    private final int capacity;

    public Room(int roomNumber, RoomCategory category, int capacity) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.capacity = capacity;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public int getCapacity() {
        return capacity;
    }

    public String toCsv() {
        return roomNumber + "," + category.name() + "," + capacity;
    }

    public static Room fromCsv(String line) {
        String[] parts = line.split(",");
        return new Room(
                Integer.parseInt(parts[0]),
                RoomCategory.valueOf(parts[1]),
                Integer.parseInt(parts[2])
        );
    }

    @Override
    public String toString() {
        return "Room " + roomNumber
                + " | " + category.getDisplayName()
                + " | Capacity: " + capacity
                + " | $" + String.format("%.2f", category.getNightlyRate()) + "/night";
    }
}
