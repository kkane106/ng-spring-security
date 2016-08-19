package controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.TodoDao;
import entities.Todo;
import entities.User;

@RestController
public class TodoController {
	@Autowired
	private TodoDao dao;
	
	@RequestMapping("/todos")
	public List<Todo> index(HttpServletRequest request) {
		User u = (User) request.getAttribute("user");
		return u.getTodos();
	}
}
