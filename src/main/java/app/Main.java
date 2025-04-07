package app;

import controller.BookingController;
import controller.TheaterController;
import org.apache.catalina.servlets.DefaultServlet;
import util.HibernateUtil;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) throws Exception {
       
        System.out.println("Initializing Hibernate...");
        HibernateUtil.getSessionFactory();
        System.out.println("Hibernate initialized");

       
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8088);

        
        String projectRoot = new File(".").getCanonicalPath();
        String docBase = new File(projectRoot, "src/main/webapp").getAbsolutePath();
        Context context = tomcat.addContext("", docBase);

      
        Tomcat.addServlet(context, "default", new DefaultServlet());
        context.addServletMappingDecoded("/static/*", "default");

     
        BookingController bookingServlet = new BookingController();
        TheaterController theaterServlet = new TheaterController();
        
        tomcat.addServlet(context, "BookingController", bookingServlet);
        tomcat.addServlet(context, "TheaterController", theaterServlet);
        
        context.addServletMappingDecoded("/api/bookings/*", "BookingController");
        context.addServletMappingDecoded("/api/theaters/*", "TheaterController");

       
        tomcat.getConnector();
        System.out.println("Starting Tomcat on http://localhost:8088");
        tomcat.start();
        tomcat.getServer().await();
    }
}