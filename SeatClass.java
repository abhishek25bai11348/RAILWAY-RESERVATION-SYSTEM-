public enum SeatClass {
    SLEEPER("Sleeper", 1.00),
    AC_THREE_TIER("AC 3 Tier", 1.65),
    AC_TWO_TIER("AC 2 Tier", 2.10),
    FIRST_CLASS("First Class", 2.75);

    private final String displayName;
    private final double multiplier;

    SeatClass(String displayName, double multiplier) {
        this.displayName = displayName;
        this.multiplier = multiplier;
    }

    public String getDisplayName() { return displayName; }
    public double getMultiplier() { return multiplier; }

    public static SeatClass fromOption(int option) {
        switch (option) {
            case 1: return SLEEPER;
            case 2: return AC_THREE_TIER;
            case 3: return AC_TWO_TIER;
            case 4: return FIRST_CLASS;
            default: throw new IllegalArgumentException("Invalid class option.");
        }
    }
}
