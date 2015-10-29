package net.apus6.streamexcept;

public class Customer {
    static class FirstNameException extends Exception {}
    static class LastNameException extends Exception {}
    private String firstName;
    private String lastName;

    public String getFirstName() throws FirstNameException {
        if (firstName.equals("Bob")) {
            throw new FirstNameException();
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() throws LastNameException {
        if (lastName.equals("something")) {
            throw new LastNameException();
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
