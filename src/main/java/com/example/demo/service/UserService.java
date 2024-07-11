package com.example.demo.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	
	public User createNewUser(User user) {
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers(){
    	return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User updateUSer(Long id, User updateduser) {
        return userRepository.findById(id).map(user -> {
        	user.setName(updateduser.getName());
        	user.setEmail(updateduser.getEmail());
        	user.setPhone(updateduser.getPhone());
        	user.setAddress(updateduser.getAddress());
            return userRepository.save(user);
        }).orElse(null);
    }
	public User updateUser(Long id, User userDetails) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		user.setEmail(userDetails.getEmail());
		user.setName(userDetails.getName());	
		user.setPhone(userDetails.getPhone());
		user.setAddress(userDetails.getAddress());
		return userRepository.save(user);
	}

	public boolean deleteUser(Long id) {
		userRepository.deleteById(id);
		return false;
	}

	public void saveUsersFromCsv(MultipartFile file) throws IOException, CsvValidationException {
		try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
			String[] line;
			List<User> users = new ArrayList<>();
			while ((line = reader.readNext()) != null) {
				User user = new User();
				user.setName(line[0]);
				user.setEmail(line[1]);
				user.setPhone(line[2]);
				user.setAddress(line[3]);
				users.add(user);
			}
			userRepository.saveAll(users);
		}
	}
}
