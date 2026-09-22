package com.example.demo.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Enrolment;
import com.example.demo.model.repository.EnrolmentRepository;

import jakarta.transaction.Transactional;

@Service
public class EnrolmentService {
	@Autowired
	EnrolmentRepository repo;
	
	@Autowired
	JWTService Jwts;
	
	
//	public Object enrol(int courseid,int instructorid, String token) {
//		Map<String, String> response = new HashMap<>();
//		Enrolment enrolment=new Enrolment();
//		try {
//			Map<String, String> parsedJWT = Jwts.validateJWT(token);
//			enrolment.setCourseid(courseid);
//			enrolment.setUserid(Integer.parseInt(parsedJWT.get("id")));
//			enrolment.setInstructorid(instructorid);
//			repo.save(enrolment);
//			response.put("message", "Enrolment done successfully");
//		}
//
//		catch (Exception e) {
//			response.put("code", "501");
//			response.put("message", e.getMessage());
//		}
//		return response;
//	}
	
	public Object enrol(int courseid, int instructorid, String token) {

	    Map<String, String> response = new HashMap<>();
	    Enrolment enrolment = new Enrolment();

	    try {
	        Map<String, String> parsedJWT = Jwts.validateJWT(token);

	        int userid = Integer.parseInt(parsedJWT.get("id"));

	        if (repo.existsByUseridAndCourseid(userid, courseid)) {
	            response.put("message", "Already enrolled in this course");
	            return response;
	        }

	        enrolment.setCourseid(courseid);
	        enrolment.setUserid(userid);
	        enrolment.setInstructorid(instructorid);

	        repo.save(enrolment);

	        response.put("message", "Enrolment done successfully");

	    } catch (Exception e) {
	        response.put("code", "501");
	        response.put("message", e.getMessage());
	    }

	    return response;
	}
	
	public Object getEnroled(String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			int loggeduserid = Integer.parseInt(parsedJWT.get("id"));
//			if (loggeduserid != userid) {
//				response.put("code", "403");
//				response.put("message", "Unauthorized");
//				return response;
//			}
			int userid = Integer.parseInt(String.valueOf(parsedJWT.get("id")));
			java.util.List<Enrolment> enrolments = repo.findByUserid(userid);
			response.put("code", "200");
			response.put("enrolments", enrolments);
		}
		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object getAllEnroled(String token) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        Map<String, String> parsedJWT = Jwts.validateJWT(token);
	        int role = Integer.parseInt(parsedJWT.get("role"));
	        if (role != 2) {
	            response.put("message", "Access denied");
	            return response;
	        }
	        java.util.List<Enrolment> enrolments = repo.findAll();
	        return enrolments;
	    } catch (Exception e) {
	        response.put("code", "501");
	        response.put("message", e.getMessage());
	        return response;
	    }
	}
	
	@Transactional
	public Object deleteEnrolment(int courseid, String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			int userid = Integer.parseInt(parsedJWT.get("id"));
			repo.deleteByUseridAndCourseid(userid, courseid);
			response.put("code", "200");
			response.put("message", "Course dropped successfully");
		}
		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}

}
