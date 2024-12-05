package WebProgramming;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final int age;
    
    public Customer(String firstName, String lastName, int age) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
    
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public int getAge() { return this.age; }
}
