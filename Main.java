public class Main {
    public static void main(String[] args) {
        try {
            RailwayReservationSystem system = new RailwayReservationSystem(new FileManager("data"));
            system.run();
        } catch (Exception e) {
            System.err.println("Unable to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
