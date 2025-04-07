package model;


import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;
    
    @Column(name = "is_booked", nullable = false)
    private boolean isBooked = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    
    @OneToOne(mappedBy = "seat", cascade = CascadeType.ALL)
    private Booking booking;

  
    public Seat() {}
    
    public Seat(String seatNumber, Screen screen) {
        this.seatNumber = seatNumber;
        this.screen = screen;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
    
    public Screen getScreen() { return screen; }
    public void setScreen(Screen screen) { this.screen = screen; }
    
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { 
        this.booking = booking;
        if (booking != null && booking.getSeat() != this) {
            booking.setSeat(this);
        }
    }
}