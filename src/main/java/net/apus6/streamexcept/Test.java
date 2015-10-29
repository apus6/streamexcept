package net.apus6.streamexcept;

import net.apus6.streamexcept.helpers.ExceptionHelpers;
import net.apus6.streamexcept.helpers.FunctionWithException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    static class CustomerNotFound extends Exception {
    }

    private List<Customer> customers = Arrays.asList(new Customer("Bob", "something"), new Customer("Boubou", "something"), new Customer("Phil", "else"));

    public Customer findCustomer(String firstName) throws CustomerNotFound {
        return customers.stream().filter(ExceptionHelpers.onExceptionFilter(c -> c.getFirstName().equals(firstName), e -> LOG.debug("First name exception"))).findFirst().orElseThrow(CustomerNotFound::new);
    }

    public void run() {
        List<String> filtered = Stream.of("Bob", "Phil", "Boubou")
                .map(ExceptionHelpers.onExceptionMap((FunctionWithException<String, Customer, Exception>) this::findCustomer))
                .peek(ExceptionHelpers.onSuccess(c -> LOG.debug("before filter GOT : " + c.toString())))
                .map(ExceptionHelpers.<Customer, String, Exception>onSuccessMapTry(Customer::getLastName))
                .filter(ExceptionHelpers.onFailureFilter(CustomerNotFound.class, e -> LOG.debug("Customer not found...")))
                .filter(ExceptionHelpers.onFailureFilter(Customer.LastNameException.class, e -> LOG.debug("Last name exception...")))
                .peek(ExceptionHelpers.onSuccess(c -> LOG.debug("after filter GOT : " + c)))
                .map(ExceptionHelpers.uneither(e -> LOG.error("unhandled exception")))
                .collect(Collectors.toList());
        filtered.forEach(s -> LOG.debug("Hi : " + s));
    }
}
