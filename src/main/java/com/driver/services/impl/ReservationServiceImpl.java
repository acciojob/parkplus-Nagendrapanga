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


        try
        {
            Reservation reservation = new Reservation();
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            reservation.setUser(user);
            List<Spot> spots = parkingLot.getSpotList();
            Spot spot = null;
            int price = Integer.MAX_VALUE;
            for (Spot s : spots) {
                int wheels;
                if (s.getSpotType() == SpotType.TWO_WHEELER) {
                    wheels = 2;
                }
                else if (s.getSpotType() == SpotType.FOUR_WHEELER) {
                    wheels = 4;
                }
                else {
                    wheels = 100;
                }
                if (!s.getOccupied() && wheels > numberOfWheels && s.getPricePerHour() < price) {
                    spot = s;
                    price = s.getPricePerHour();
                }
            }
            if (spot == null) {
                throw new Exception();
            }
            reservation.setSpot(spot);
            reservation.setNumberOfHours(timeInHours);
            spot.setOccupied(true);
            user.getReservationList().add(reservation);
            spot.getReservationList().add(reservation);
            spotRepository3.save(spot);
            userRepository3.save(user);
            return reservation;
        }
        catch (Exception e) {
            return null;
        }

    }

}
