package com.klu.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klu.springmvc.model.Task;
import com.klu.springmvc.service.TaskService;

@CrossOrigin
@RestController
@RequestMapping("/ms2")
public class MS2Controller {
  @Autowired
  TaskService ts;
  
  @GetMapping("/mul")
  public String mul(@RequestParam("a") int a,
      @RequestParam("b") int b) {
    
    int result=a*b;
    return "MS2.1 multiplication"+result;
  }
  
  @PostMapping("/createtask")
  public Object createTask(@RequestBody Task task,
      @RequestHeader("Token") String token) {
    
    return ts.createTask(task,token);
  }
  
  @DeleteMapping("/deletetask/{ID}")
  public Object createTask(@PathVariable("ID") int id,
      @RequestHeader("Token") String token) {
    
    return ts.deleteTask(id,token);
  }
  
  @PutMapping("/updatetask/{ID}")
  public Object createTask(@PathVariable("ID") int id,
	  @RequestBody Task task,
      @RequestHeader("Token") String token) {
    
    return ts.updateTask(id,task ,token);
  }
  
  @GetMapping("/gettask/{ID}")
  public Object getTask(@PathVariable("ID") int id,
      @RequestHeader("Token") String token) {
    
    return ts.getTask(id,token);
  }
  
  @GetMapping("/getalltasks/{PAGE}/{SIZE}")
  public Object getTask(@PathVariable("PAGE") int page,
	  @PathVariable("SIZE") int size,
      @RequestHeader("Token") String token) {
    
      return ts.getAllTasks(page,size,token);
  }
  
  
  
}