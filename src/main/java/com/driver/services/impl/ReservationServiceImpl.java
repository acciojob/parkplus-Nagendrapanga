package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {


            if(!userRepository3.existsById(userId))
                throw new Exception("Cannot make reservation");
            if(!parkingLotRepository3.existsById(parkingLotId))
                throw new Exception("Cannot make reservation");

            User user = userRepository3.findById(userId).get();

            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();


            List<Spot> spots = parkingLot.getSpotList();
            Spot ospot = null;
            int price = Integer.MAX_VALUE;
            for (Spot sp : spots) {
                int wheels;
                if (sp.getSpotType() == SpotType.TWO_WHEELER) {
                    wheels = 2;
                } else if (sp.getSpotType() == SpotType.FOUR_WHEELER) {
                    wheels = 4;
                } else {
                    wheels = 100;
                }
                if (!sp.getOccupied() && wheels > numberOfWheels && sp.getPricePerHour()< price) {
                    ospot = sp;
                    price = sp.getPricePerHour();
                }
            }
            if(ospot==null)
                throw new Exception("Cannot make reservation");

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setUser(user);
            reservation.setSpot(ospot);
            ospot.setOccupied(true);
            ospot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);
            reservationRepository3.save(reservation);
            return reservation;

    }

}
