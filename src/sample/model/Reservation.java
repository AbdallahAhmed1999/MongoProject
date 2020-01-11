package sample.model;

import java.time.LocalDateTime;

public class Reservation {
    private Appointment appointment;

    public Reservation(String type){
        if (type.equalsIgnoreCase("special")){
            appointment = new SpecialAppointment();
        }else if(type.equalsIgnoreCase("regular")){
            appointment = new RegularAppointment();
        }
    }

    public LocalDateTime reserve(){
        return appointment.request();
    }


}
