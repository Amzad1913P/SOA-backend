package com.example.demo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Payment;
import com.example.demo.model.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository repo;

    @Autowired
    JWTService Jwts;

    public Object createPayment(int enrolmentid, int amount, String status, String token) {
        Map<String, String> response = new HashMap<>();
        try {
            Map<String, String> parsedJWT = Jwts.validateJWT(token);
            int userid = Integer.parseInt(parsedJWT.get("id"));
            List<Payment> payments = repo.findByUserid(userid);
            for (Payment p : payments) {
                if (p.getEnrolmentid() == enrolmentid && p.getStatus().equalsIgnoreCase("payed")) {
                    response.put("message", "Already payed");
                    return response;
                }
            }
            Payment payment = new Payment();
            payment.setEnrolmentid(enrolmentid);
            payment.setUserid(userid);
            payment.setAmount(amount);
            payment.setStatus(status);
            repo.save(payment);
            response.put("message", "Amount payed to the course.");
        } catch (Exception e) {
            response.put("code", "501");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Object getUserPayments(String token) {

        Map<String, Object> response = new HashMap<>();

        try {

            Map<String, String> parsedJWT = Jwts.validateJWT(token);

            int userid = Integer.parseInt(parsedJWT.get("id"));

            List<Payment> payments = repo.findByUserid(userid);

            response.put("message", "Payment history retrieved successfully");
            response.put("payments", payments);

        } catch (Exception e) {

            response.put("code", "501");
            response.put("message", e.getMessage());

        }

        return response;
    }

    public Object getAllPayments(String token) {

        Map<String, Object> response = new HashMap<>();

        try {

            Map<String, String> parsedJWT = Jwts.validateJWT(token);

            String role = parsedJWT.get("role");

            if (!role.equals("2")) {

                response.put("code", "403");
                response.put("message", "Access denied. Admin only.");

                return response;
            }

            List<Payment> payments = repo.findAll();

            response.put("message", "All payments retrieved successfully");
            response.put("payments", payments);

        } catch (Exception e) {

            response.put("code", "501");
            response.put("message", e.getMessage());

        }

        return response;
    }
}