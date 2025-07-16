package com.example.demo.generator;

import com.example.demo.model.Rental;
import com.example.demo.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class ReportGenerator {

    private final RentalRepository rentalRepository;
    private final Calendar calendar;

    public ReportGenerator(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
        this.calendar = Calendar.getInstance();
    }

    public void generateReport(Integer maxDaysAllowed) {
        calendar.add(Calendar.DAY_OF_MONTH, -maxDaysAllowed);
        Date marginDate = calendar.getTime();
        for(Rental rental : rentalRepository.findByRentalDateBefore(marginDate)){
            
        }

    }
}
