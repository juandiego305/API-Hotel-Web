package com.juancho12.Api_Hotel.services;

import com.juancho12.Api_Hotel.models.Customer;
import com.juancho12.Api_Hotel.models.Reservation;
import com.juancho12.Api_Hotel.models.Room;
import com.juancho12.Api_Hotel.repositories.CustomerRepository;
import com.juancho12.Api_Hotel.repositories.ReservationRepository;
import com.juancho12.Api_Hotel.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Crear una reserva
    public Reservation createReservation(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Room> room = roomRepository.findById(roomId);

        if (customer.isEmpty() || room.isEmpty()) {
            throw new RuntimeException("Cliente o habitación no encontrados.");
        }

        // Verificar disponibilidad de la habitación
        List<Reservation> existingReservations = reservationRepository.findByRoomId(roomId);
        for (Reservation res : existingReservations) {
            if (!(endDate.isBefore(res.getStartDate()) || startDate.isAfter(res.getEndDate()))) {
                throw new RuntimeException("La habitación ya está reservada en esta fecha.");
            }
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer.get());
        reservation.setRoom(room.get());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        return reservationRepository.save(reservation);
    }

    // Obtener una reserva por ID
    public Optional<Reservation> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    // Obtener todas las reservas de un cliente
    public List<Reservation> getReservationsByCustomer(Long customerId) {
        return reservationRepository.findByCustomerId(customerId);
    }

    // Obtener todas las reservas de una habitación
    public List<Reservation> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    // Actualizar fechas de una reserva
    public Reservation updateReservationDate(Long reservationId, LocalDate newStartDate, LocalDate newEndDate) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            // Verificar disponibilidad en las nuevas fechas
            List<Reservation> existingReservations = reservationRepository.findByRoomId(reservation.getRoom().getId());
            for (Reservation res : existingReservations) {
                if (!res.getId().equals(reservationId) &&
                        !(newEndDate.isBefore(res.getStartDate()) || newStartDate.isAfter(res.getEndDate()))) {
                    throw new RuntimeException("Las nuevas fechas se superponen con otra reserva.");
                }
            }

            reservation.setStartDate(newStartDate);
            reservation.setEndDate(newEndDate);
            return reservationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reserva no encontrada."));
    }

    // Cancelar una reserva
    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new RuntimeException("Reserva no encontrada.");
        }
        reservationRepository.deleteById(reservationId);
    }
}
