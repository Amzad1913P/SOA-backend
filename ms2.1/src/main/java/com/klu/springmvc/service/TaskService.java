package com.klu.springmvc.service;

import java.awt.List;
import org.springframework.data.domain.Pageable;
//import java.awt.print.Pageable;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.klu.springmvc.model.Task;
import com.klu.springmvc.repository.TaskRepository;

import io.jsonwebtoken.Jwts;


@Service
public class TaskService {
	@Autowired
	JWTService jwts;
	@Autowired
	TaskRepository repo;

	public Object createTask(Task task, String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = jwts.validateJWT(token);
			task.setCreatedby(Integer.parseInt(parsedJWT.get("id")));
			//task.setId((int)repo.count()+1);
			repo.save(task);
			response.put("message", "Task created successfully by MS2.1");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object deleteTask(int id, String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = jwts.validateJWT(token);
			Task task=repo.findById(id).get();
//			if(task==null) {
//				response.put("code", "404");
//				response.put("message", "task does not exits"); -> no need goes to catch
//				return response;
//			}
			repo.deleteById(id);
			//repo.delete(task);
			response.put("code", "200");
			response.put("message", "Task deleted successfully");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object updateTask(int id,Task intask , String token) {
		Map<String, String> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = jwts.validateJWT(token);
			Task task=repo.findById(id).get();
//			if(task==null) {
//				response.put("code", "404");
//				response.put("message", "task does not exits");---> no need goes to catch
//				return response;
//			}
			task.setTitle(intask.getTitle());
			task.setDescription(intask.getDescription());
			task.setDeadline(intask.getDeadline().toString());
			task.setPriority(intask.getPriority());
			repo.save(task);
			response.put("message", "Task updated successfully");
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	public Object getTask(int id, String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = jwts.validateJWT(token);
			Task task=repo.findById(id).get();
			if(task==null) {
				response.put("code", "404");
				response.put("message","task not found");
				return response;
			}
			response.put("code",200);
			//response.put("message","task not found");
			response.put("task",task);
			return response;
		}

		catch (Exception e) {
			response.put("code", "501");
			response.put("message", e.getMessage());
		}
		return response;
	}
	
//	public Object getalltasks(int page,int size , String token) {
//		Map<String, Object> response = new HashMap<>();
//		try {
//			Map<String, String> parsedJWT = jwts.validateJWT(token);
//			int rows=(int)repo.count();
//			int totalpages=rows/size+rows%size;
//			int first=(page*size)-size+1;
//			int last=size*page+size;
//			if(last<rows) {
//				last=rows;
//			}
//			else if(first>rows) {
//				response.put("end", 0);
//			}
//			java.util.List<Task> tasks=repo.findByIdBetween(first,last);
////			if(task==null) {
////				response.put("code", "404");
////				response.put("message", "task does not exits");---> no need goes to catch
////				return response;
////			}
//			response.put("message", "Task updated successfully");
//			response.put("tasks", tasks);
//		}
//
//		catch (Exception e) {
//			response.put("code", "501");
//			response.put("message", e.getMessage());
//		}
//		return response;
//	}
	
	public Object getAllTasks(int page, int size, String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, String> parsedJWT = jwts.validateJWT(token);
//		Pageable pageable = PageRequest.of(page-1, size);
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<Task> tasks = repo.findAll(pageable);
			response.put("code", 200);
			response.put("page", page);
			response.put("size", size);
			response.put("totalpages", tasks.getTotalPages());
			response.put("tasks", tasks.getContent());
			return response;
		} catch (Exception e) {
			response.put("code", 500);
			response.put("message", e.getMessage());
			return response;
		}
	}

}