package BankApp;

import Domain.Customer;

public class Session {
    private static Customer currentUser;

    public static void setCurrentUser(Customer user) {
        currentUser = user;
    }

    public static Customer getCurrentUser() {
        return currentUser;
    }
}

