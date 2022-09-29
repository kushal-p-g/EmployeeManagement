package com.employee.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee.manage.entity.Employee;
import com.employee.manage.entity.User;
import com.employee.manage.repository.UserRepo;
import com.employee.manage.service.EmployeeService;


@Controller
public class AppController {
	
	   @Autowired
	   private EmployeeService employeeService;
	   
 
    @Autowired
    private UserRepo userRepo;
     
    @GetMapping("/employeeManagement")
    public String viewHomePage() {
        return "index";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
         
        return "signup_form";
    }
    @RequestMapping(value = "/process_register", method = { RequestMethod.GET, RequestMethod.POST })
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
         
        userRepo.save(user);
         
        return "registration_success";
    }
    @RequestMapping(value = "/employee", method = { RequestMethod.GET, RequestMethod.POST })
    public String viewEmployee(Model model) {
    	model.addAttribute(findPaginated(1,"firstName","asc", model));
//    	model.addAttribute("listEmployee", employeeService.getAllEmployee());
    	return "Employee.html"; 	
    }
    @GetMapping(value = "/newEmployeeform")
   	public String showNewEmployeeForm(Model model) {
    	Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}
    @RequestMapping(value = "/saveEmployee", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveEmployee(@ModelAttribute("employee") Employee employee) {

    employeeService.saveEmployee(employee);
	return "redirect:/employee";
    }
 
    @RequestMapping(value = "/showFormForUpdate/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		Employee employee = employeeService.getEmployeeById(id);
		
		model.addAttribute("employee", employee);
		return "update_employee";

	}
    @RequestMapping(value = "/deleteEmployee/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteEmployee(@PathVariable (value = "id") long id) {
		
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/employee";
	}
    @GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployee = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		
		model.addAttribute("listEmployee", listEmployee);
		return "Employee";
	}
		
}