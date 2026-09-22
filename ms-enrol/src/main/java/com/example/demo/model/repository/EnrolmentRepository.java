package com.example.demo.model.repository;

import java.awt.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Enrolment;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {
	java.util.List<Enrolment> findByUserid(int userid);

	void deleteByUseridAndCourseid(int userid, int courseid);
	
	Boolean existsByUseridAndCourseid(int userid, int courseid);

}
