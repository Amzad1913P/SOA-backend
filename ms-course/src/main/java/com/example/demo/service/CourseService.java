package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;


import io.jsonwebtoken.Jwts;

@Service
public class CourseService {
	@Autowired
	JWTService Jwts;
	
	@Autowired
	CourseRepository repo;
	//CourseRepository repo;
	
	public Object addCourse(Course course, String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			String role = parsedJWT.get("role");
			if (!role.equals("2")) {

                response.put("code", "403");
                response.put("message", "Access denied. Admin only.");

                return response;
            }
			repo.save(course);
			//repo.save(course);
			response.put("message", "Course created successfully by MS2.1");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object deleteCourse(int id, String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			Course course =repo.findById(id).get();
			repo.deleteById(id);
			response.put("code", "200");
			response.put("message", "Course deleted successfully");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object updateCourse(int id,Course incourse , String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			String role = parsedJWT.get("role");

            if (!role.equals("2")) {

                response.put("code", "403");
                response.put("message", "Access denied. Admin only.");

                return response;
            }
			Course course=repo.findById(id).get();
			course.setName(incourse.getName());
			course.setInstructor(incourse.getInstructor());
			course.setInstructorid(incourse.getInstructorid());
			course.setCoursecode(incourse.getCoursecode().toString());
			repo.save(course);
			response.put("message", "Course updated successfully");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object getCourse(int id, String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			Course course=repo.findById(id).get();
			if(course==null) {
				response.put("code", "404");
				response.put("message","course not found");
				return response;
			}
			response.put("code",200);
			response.put("task",course);
			return response;
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object getAllCourses(int page, int size, String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = Jwts.validateJWT(token);
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<Course> courses = repo.findAll(pageable);
			response.put("code", 200);
			response.put("page", page);
			response.put("size", size);
			response.put("totalpages", courses.getTotalPages());
			response.put("tasks", courses.getContent());
			return response;
		} catch (Exception e) {
			response.put("code", 500);
			response.put("message", e.getMessage());
			return response;
		}
	}
	

}
