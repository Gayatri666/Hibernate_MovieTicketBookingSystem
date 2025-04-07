package controller;


import model.Theater;
import util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/theaters")
public class TheaterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Theater> theaters = session.createQuery("FROM Theater", Theater.class).list();
            
            try (PrintWriter out = resp.getWriter()) {
                out.print("[");
                for (int i = 0; i < theaters.size(); i++) {
                    Theater t = theaters.get(i);
                    out.print("{");
                    out.print("\"id\":" + t.getId() + ",");
                    out.print("\"name\":\"" + escapeJson(t.getName()) + "\",");
                    out.print("\"location\":\"" + escapeJson(t.getLocation()) + "\",");
                    out.print("\"capacity\":" + t.getCapacity());
                    out.print("}");
                    if (i < theaters.size() - 1) out.print(",");
                }
                out.print("]");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String location = req.getParameter("location");
        String capacityStr = req.getParameter("capacity");
        
        if (name == null || location == null || capacityStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }
        
        try {
            int capacity = Integer.parseInt(capacityStr);
            Theater theater = new Theater(name, location, capacity);
            
            Transaction tx = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                tx = session.beginTransaction();
                session.persist(theater);
                tx.commit();
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid capacity format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}

