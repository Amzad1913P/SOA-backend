package com.example.demo;


import com.example.demo.model.Enrolment;
import com.example.demo.model.repository.EnrolmentRepository;
import com.example.demo.model.service.EnrolmentService;
import com.example.demo.model.service.JWTService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EnrolmentServiceTest {

    @Mock
    private EnrolmentRepository repo;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private EnrolmentService enrolmentService;

    @BeforeEach
    void setUp() {
        // Dependencies are injected by Mockito.
    }

    @Test
    void enrol_whenValidToken_savesEnrolment() throws Exception {

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.existsByUseridAndCourseid(10, 101))
                .thenReturn(false);

        Object result = enrolmentService.enrol(101, 5, "valid-token");

        verify(repo).save(argThat(e ->
                e.getUserid() == 10
                && e.getCourseid() == 101
                && e.getInstructorid() == 5
        ));

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals(
                "Enrolment done successfully",
                response.get("message")
        );
    }

    @Test
    void enrol_whenAlreadyEnrolled_doesNotSaveAgain()
            throws Exception {

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.existsByUseridAndCourseid(10, 101))
                .thenReturn(true);

        Object result = enrolmentService.enrol(101, 5, "valid-token");

        verify(repo, never()).save(any(Enrolment.class));

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals(
                "Already enrolled in this course",
                response.get("message")
        );
    }

    @Test
    void getEnroled_returnsLoggedInUsersEnrollments()
            throws Exception {

        Enrolment enrolment = new Enrolment();
        enrolment.setUserid(10);
        enrolment.setCourseid(101);
        enrolment.setInstructorid(5);

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        when(repo.findByUserid(10))
                .thenReturn(List.of(enrolment));

        Object result = enrolmentService.getEnroled("valid-token");

        verify(repo).findByUserid(10);

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals("200", response.get("code"));
        assertEquals(List.of(enrolment), response.get("enrolments"));
    }

    @Test
    void getAllEnroled_whenAdmin_returnsAllEnrollments()
            throws Exception {

        Enrolment enrolment = new Enrolment();
        enrolment.setUserid(10);
        enrolment.setCourseid(101);
        enrolment.setInstructorid(5);

        when(jwtService.validateJWT("admin-token"))
                .thenReturn(Map.of("id", "1", "role", "2"));

        when(repo.findAll()).thenReturn(List.of(enrolment));

        Object result = enrolmentService.getAllEnroled("admin-token");

        verify(repo).findAll();

        assertEquals(List.of(enrolment), result);
    }

    @Test
    void getAllEnroled_whenNotAdmin_deniesAccess()
            throws Exception {

        when(jwtService.validateJWT("student-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        Object result = enrolmentService.getAllEnroled("student-token");

        verify(repo, never()).findAll();

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals("Access denied", response.get("message"));
    }

    @Test
    void deleteEnrolment_deletesLoggedInUsersCourse()
            throws Exception {

        when(jwtService.validateJWT("valid-token"))
                .thenReturn(Map.of("id", "10", "role", "1"));

        Object result =
                enrolmentService.deleteEnrolment(101, "valid-token");

        verify(repo).deleteByUseridAndCourseid(10, 101);

        Map<?, ?> response = assertInstanceOf(Map.class, result);

        assertEquals("200", response.get("code"));
        assertEquals(
                "Course dropped successfully",
                response.get("message")
        );
    }
}