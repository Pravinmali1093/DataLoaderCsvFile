package com.example.demo.controller;



import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
//@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping(value = "/addUser")
	public ResponseEntity<String> createNewUser(@RequestBody User user) {
		System.out.println("User Data Add : " + user);
		userService.createNewUser(user);
		return new ResponseEntity<String>("User Added Successfully...!!!.", HttpStatus.CREATED);
	}

	@GetMapping(value = "/getAllUser")
	public ResponseEntity<List<User>> getAllUser() {
		List<User>user=userService.getAllUsers();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id){
		User user=userService.getUserById(id);
		return user !=null ? new ResponseEntity<>(user, HttpStatus.OK) :new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateStudentRecord(id, updatedUser);
        return user != null ? new ResponseEntity<>("User Updated...!!!", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    	 boolean isDeleted = userService.deleteUser(id);
         return isDeleted ? new ResponseEntity<>("User Not Found...!!!",HttpStatus.NOT_FOUND) :new ResponseEntity<>("User Deleted...!!!",HttpStatus.OK);
         
    }
    

    @PostMapping("/upload")
    public ResponseEntity<String> uploadUsersFromCsv(@RequestParam("file") MultipartFile file) {
        try {
            userService.saveUsersFromCsv(file);
            return new ResponseEntity<>("File Upload Successfully..!!",HttpStatus.OK);
        } catch (IOException | CsvValidationException e) {
            return  new ResponseEntity<>("Internal Server Error..!!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}