package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {
    Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        var lastCustomer = customers.pollLast();
        if (lastCustomer == null){
            return null;
        }
        return new Customer(lastCustomer.getId(), lastCustomer.getName(), lastCustomer.getScores());
    }
}
