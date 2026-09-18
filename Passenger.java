public class Passenger extends Person {
    private final String passengerId;
    private final int age;
    private final String gender;

    public Passenger(String passengerId, String name, int age, String gender, String phone) {
        super(name, phone);
        this.passengerId = passengerId;
        this.age = age;
        this.gender = gender;
    }

    public String getPassengerId() { return passengerId; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
}
