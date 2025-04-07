package model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "screens")
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(name = "show_time", nullable = false)
    private LocalDateTime showTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;
    
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();
    
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    // Constructors
    public Screen() {}
    
    public Screen(String name, LocalDateTime showTime, Movie movie, Theater theater) {
        this.name = name;
        this.showTime = showTime;
        this.movie = movie;
        this.theater = theater;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }
    
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    
    public Theater getTheater() { return theater; }
    public void setTheater(Theater theater) { this.theater = theater; }
    
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
    
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
    

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setScreen(this);
    }
    
    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setScreen(null);
    }
    
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setScreen(this);
    }
}