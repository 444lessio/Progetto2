package com.hotel_alduina.hotel_management.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.hotel_alduina.hotel_management.dto.BookingForm;
import com.hotel_alduina.hotel_management.model.Booking;
import com.hotel_alduina.hotel_management.model.GuestDetail;
import com.hotel_alduina.hotel_management.model.Room;
import com.hotel_alduina.hotel_management.model.RoomStatus;
import com.hotel_alduina.hotel_management.model.User;
import com.hotel_alduina.hotel_management.repository.BookingRepository;
import com.hotel_alduina.hotel_management.repository.GuestDetailRepository;
import com.hotel_alduina.hotel_management.repository.RoomRepository;
import com.hotel_alduina.hotel_management.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuestDetailRepository guestDetailRepository;

    /* Calcolo costo totale */
    public double calculateTotalCost(BookingForm form) {

        if (form.getStartDate() == null || form.getEndDate() == null) {
            throw new IllegalArgumentException("Le date di ckec-in e di check-out non possono essere nulle");
        }

        Room room = roomRepository.findById(form.getRoomId())
                .orElseThrow(() -> new RuntimeException("Camera non trovata"));

        long nights = ChronoUnit.DAYS.between(
                form.getStartDate(),
                form.getEndDate()
        );

        if (nights <= 0) {
            nights = 1;
        }

        double baseTotal = room.getPricePerNight() * nights;
        double additionalCosts = 0.0;

        String services = form.getAdditionalServices();
        if (services != null) {
            String s = services.toLowerCase();

            if (s.contains("spa")) {
                additionalCosts += 50.0;
            }
            if (s.contains("transfer")) {
                additionalCosts += 30.0;
            }
            if (s.contains("colazione")) {
                additionalCosts += 15.0;
            }
        }

        return baseTotal + additionalCosts;
    }

    /* Salvataggio prenotazione */
    public Booking saveBooking(BookingForm form, String username) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Utente non trovato"));

    Room room = roomRepository.findById(form.getRoomId())
            .orElseThrow(() -> new RuntimeException("Camera non trovata"));

    Booking booking = new Booking();
    booking.setCustomer(user);
    booking.setRoom(room);
    booking.setStartDate(form.getStartDate());
    booking.setEndDate(form.getEndDate());
    booking.setAdditionalServices(form.getAdditionalServices());
    booking.setTotalCost(calculateTotalCost(form));
    booking.setCheckedIn(false);
    booking.setCheckedOut(false);
    booking.setNumGuests(form.getNumGuests());

    // SALVA LA PRENOTAZIONE PRIMA
    Booking savedBooking = bookingRepository.save(booking);

    // AGGIUNGI: Salva gli ospiti
    if (form.getGuests() != null && !form.getGuests().isEmpty()) {
        List<GuestDetail> guestDetails = form.getGuests().stream()
            .map(dto -> {
                GuestDetail guest = new GuestDetail();
                guest.setFirstName(dto.getFirstName());
                guest.setLastName(dto.getLastName());
                guest.setCitizenship(dto.getCitizenship());
                guest.setBirthPlace(dto.getBirthPlace());
                guest.setBirthDate(LocalDate.parse(dto.getBirthDate()));
                guest.setLeader(dto.isLeader());
                guest.setDocumentType(dto.getDocumentType());
                guest.setDocumentNumber(dto.getDocumentNumber());
                guest.setExemptionType(dto.getExemptionType());
                guest.setBooking(savedBooking);
                return guest;
            })
            .collect(Collectors.toList());
        
        guestDetailRepository.saveAll(guestDetails);
    }

    return savedBooking;
}

    /* Check-in */
    public void performCheckIn(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        booking.setCheckedIn(true);

        Room room = booking.getRoom();
        room.setStatus(RoomStatus.OCCUPATA);

        roomRepository.save(room);
        bookingRepository.save(booking);
    }

    public Booking performCheckOut(Long bookingId) {
        Booking booking = findBookingById(bookingId);

        if (!booking.isCheckedIn()) {
            throw new IllegalStateException("Check-out non possibile: check-in non effettuato");
        }

        // Aggiorno lo stato della camera
        Room room = booking.getRoom();
        if (room != null) {
            room.setStatus(RoomStatus.DA_PULIRE); // libera la camera
            roomRepository.save(room);
        }

        // Aggiorno lo stato della prenotazione
        booking.setCheckedIn(false); // check-out = non più check-in
        bookingRepository.save(booking);

        return booking; // restituisco l'oggetto per mostrare i dati a Thymeleaf
    }

    /* Prenotazioni attive */
    public Collection<Booking> getActiveBookings() {
        return bookingRepository.findByCheckedInTrueAndCheckedOutFalse();
    }

    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));
    }
}
