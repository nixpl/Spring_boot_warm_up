package com.example.demo.scheduler;

import com.example.demo.model.Customer;
import com.example.demo.model.Rental;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Slf4j
public class Scheduler {

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;

    public Scheduler(RentalRepository rentalRepository, CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
    }

    protected int calculateDaysDiff(Date dateA, Date dateB)
    {
        LocalDate localDateA = dateA.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateB = dateB.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return (int) ChronoUnit.DAYS.between(localDateA, localDateB);
    }

    @Scheduled(cron = "0 0 7 ? * MON")
//    @Scheduled(cron = "0/30 * * * * ?")
    @Transactional
    protected void generateDelayedReturnsReport() {
        Integer maxDaysAllowed = 7;
        Date dateNow = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -maxDaysAllowed);
        Date marginDate = calendar.getTime();

        List<Rental> delayedRentals = rentalRepository.findDelayedRentals(marginDate);

        if (delayedRentals.isEmpty()) {
            log.info("No delayed rentals found. Skipping report generation.");
            return;
        }

        Map<Customer, List<String>> delayedFilmsFromCustomerMap = new HashMap<>();

        for (Rental delayedRental : delayedRentals) {
            Customer customer = delayedRental.getCustomer();
            delayedFilmsFromCustomerMap.computeIfAbsent(customer, k -> new ArrayList<>());

            int rentalDaysDiff = calculateDaysDiff(delayedRental.getRentalDate(), dateNow);

            delayedFilmsFromCustomerMap.get(customer).add(
                    delayedRental.getInventory().getFilm().getTitle() +
                            " (rentalDays: " + rentalDaysDiff + ")"
            );
        }

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Customers with delayed returns:\n");

        for (Map.Entry<Customer, List<String>> entry : delayedFilmsFromCustomerMap.entrySet()) {
            Customer customer = entry.getKey();
            reportBuilder.append(String.format(
                    "- firstName: %s, lastName: %s, email: %s, delayedFilms: %s\n",
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    entry.getValue()
            ));
        }

        log.info(reportBuilder.toString());
    }



    @Scheduled(cron = "0 0 23 * * ?")
//    @Scheduled(cron = "0/30 * * * * ?")
    @Transactional
    public void deactivateInactiveCustomers() {
        log.info("Started scheduled task: Deactivating inactive customers. Time: {}", LocalDate.now());

        Date nMonthsAgo = Date.from(LocalDate.now().minusMonths(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Integer> customerIdsToDeactivate = customerRepository.findCustomerIdsToDeactivate(nMonthsAgo);

        if (customerIdsToDeactivate.isEmpty()) {
            log.info("No customers found for deactivation.");
        } else {
            int updatedCount = customerRepository.deactivateCustomers(customerIdsToDeactivate);
            log.info("Deactivation completed. Number of customers deactivated: {}", updatedCount);
            log.info("Deactivated customer IDs: {}", customerIdsToDeactivate);
        }

        log.info("Finished scheduled task: Deactivating inactive customers.");
    }

}
