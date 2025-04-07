package controller;


import model.Booking;
import model.Screen;
import model.Seat;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/api/bookings")
public class BookingController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String screenIdStr = req.getParameter("screenId");
        String seatIdStr = req.getParameter("seatId");
        String amountStr = req.getParameter("amount");
        
        if (screenIdStr == null || seatIdStr == null || amountStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }
        
        try {
            Long screenId = Long.parseLong(screenIdStr);
            Long seatId = Long.parseLong(seatIdStr);
            double amount = Double.parseDouble(amountStr);
            
            Transaction tx = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                
                Screen screen = session.get(Screen.class, screenId);
                Seat seat = session.get(Seat.class, seatId);
                
                if (screen == null || seat == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Screen or seat not found");
                    return;
                }
                
                if (seat.isBooked()) {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Seat already booked");
                    return;
                }
                
                Booking booking = new Booking(amount, "CONFIRMED", screen, seat);
                session.persist(booking);
                
                tx.commit();
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}