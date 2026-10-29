package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Payment;
import com.example.demo.model.repository.PaymentRepository;
import com.example.demo.model.service.JWTService;
import com.example.demo.model.service.PaymentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repo;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment_whenValidToken_savesPayment()
            throws Exception {

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.findByUserid(10)).thenReturn(List.of());

        Object result = paymentService.createPayment(
                501, 2000, "pending", "valid-token"
        );

        verify(repo).save(argThat(p ->
                p.getUserid() == 10
                && p.getEnrolmentid() == 501
                && p.getAmount() == 2000
                && "pending".equals(p.getStatus())
        ));

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals(
                "Amount payed to the course.",
                response.get("message")
        );
    }

    @Test
    void createPayment_whenAlreadyPaid_doesNotSaveAgain()
            throws Exception {

        Payment existingPayment = new Payment();
        existingPayment.setUserid(10);
        existingPayment.setEnrolmentid(501);
        existingPayment.setAmount(2000);
        existingPayment.setStatus("payed");

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.findByUserid(10))
                .thenReturn(List.of(existingPayment));

        Object result = paymentService.createPayment(
                501, 2000, "payed", "valid-token"
        );

        verify(repo, never()).save(any(Payment.class));

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals("Already payed", response.get("message"));
    }

    @Test
    void getUserPayments_returnsPaymentHistory()
            throws Exception {

        Payment payment = new Payment();
        payment.setUserid(10);
        payment.setEnrolmentid(501);
        payment.setAmount(2000);
        payment.setStatus("payed");

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.findByUserid(10)).thenReturn(List.of(payment));

        Object result = paymentService.getUserPayments("valid-token");

        verify(repo).findByUserid(10);

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals(
                "Payment history retrieved successfully",
                response.get("message")
        );

        assertEquals(List.of(payment), response.get("payments"));
    }

    @Test
    void getAllPayments_whenAdmin_returnsAllPayments()
            throws Exception {

        Payment payment = new Payment();
        payment.setUserid(10);
        payment.setEnrolmentid(501);
        payment.setAmount(2000);
        payment.setStatus("payed");

        when(jwtService.validateJWT("admin-token"))
                .thenReturn(Map.of("id", "1", "role", "2"));

        when(repo.findAll()).thenReturn(List.of(payment));

        Object result = paymentService.getAllPayments("admin-token");

        verify(repo).findAll();

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals(
                "All payments retrieved successfully",
                response.get("message")
        );

        assertEquals(List.of(payment), response.get("payments"));
    }

    @Test
    void getAllPayments_whenNotAdmin_deniesAccess()
            throws Exception {

        when(jwtService.validateJWT("student-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        Object result = paymentService.getAllPayments("student-token");

        verify(repo, never()).findAll();

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals("403", response.get("code"));
        assertEquals(
                "Access denied. Admin only.",
                response.get("message")
        );
    }
}