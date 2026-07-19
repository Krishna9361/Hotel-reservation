package hotel;

public enum RoomCategory {
    STANDARD("Standard", 120.00),
    DELUXE("Deluxe", 190.00),
    SUITE("Suite", 320.00);

    private final String displayName;
    private final double nightlyRate;

    RoomCategory(String displayName, double nightlyRate) {
        this.displayName = displayName;
        this.nightlyRate = nightlyRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getNightlyRate() {
        return nightlyRate;
    }

    public static RoomCategory fromMenuChoice(int choice) {
        switch (choice) {
            case 1:
                return STANDARD;
            case 2:
                return DELUXE;
            case 3:
                return SUITE;
            default:
                throw new IllegalArgumentException("Invalid category choice.");
        }
    }
}
