package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;
    
    @Column(name = "total_amount", nullable = false)
    private double totalAmount;
    
    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    
    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    // Constructors
    public Booking() {
        this.bookingTime = LocalDateTime.now();
    }
    
    public Booking(double totalAmount, String paymentStatus, Screen screen, Seat seat) {
        this();
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.screen = screen;
        this.seat = seat;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public Screen getScreen() { return screen; }
    public void setScreen(Screen screen) { this.screen = screen; }
    
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { 
        this.seat = seat;
        seat.setBooked(true);
        if (seat.getBooking() != this) {
            seat.setBooking(this);
        }
    }
}