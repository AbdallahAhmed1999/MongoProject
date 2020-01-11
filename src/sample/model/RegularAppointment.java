package sample.model;

import java.time.LocalDateTime;

public class RegularAppointment implements Appointment{
    @Override
    public LocalDateTime request() {
        return LocalDateTime.now().plusDays(7);
    }
}
