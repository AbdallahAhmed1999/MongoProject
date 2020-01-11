package sample.model;

import java.time.LocalDateTime;

public class SpecialAppointment implements Appointment {
    @Override
    public LocalDateTime request() {
        return LocalDateTime.now().plusDays(1);
    }
}
