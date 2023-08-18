package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        var entry = customers.firstEntry();
        return copyOrNull(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var next = customers.higherEntry(customer);
        return copyOrNull(next);
    }

    public void add(Customer customer, String data) {
        this.customers.put(customer, data);
    }

    private Map.Entry<Customer, String> copyOrNull(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }
        return Map.entry(
                new Customer(entry.getKey().getId(), entry.getKey().getName(), entry.getKey().getScores()),
                entry.getValue()
        );
    }
}
