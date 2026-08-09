package com.sqlmind.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sqlmind.dto.request.LoginRequest;
import com.sqlmind.dto.request.RegisterRequest;
import com.sqlmind.dto.response.LoginResponse;
import com.sqlmind.dto.response.RegisterResponse;
import com.sqlmind.entity.User;
import com.sqlmind.exception.InvalidCredentialsException;
import com.sqlmind.exception.UserAlreadyExistsException;
import com.sqlmind.repository.UserRepository;
import com.sqlmind.security.JwtService;
import com.sqlmind.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
        
	@Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        	throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();

        BeanUtils.copyProperties(savedUser, response);

        return response;
    }
	
	@Override
	public LoginResponse login(LoginRequest request) {

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new InvalidCredentialsException("Invalid email or password"));

	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new InvalidCredentialsException("Invalid email or password");
	    }

	    LoginResponse response = new LoginResponse();
	    
	    String token = jwtService.generateToken(
	            user.getId(),
	            user.getEmail(),
	            user.getRole()
	    );

	    response.setAccessToken(token);
	    response.setTokenType("Bearer");
	    response.setUserId(user.getId());
	    response.setFullName(user.getFullName());
	    response.setEmail(user.getEmail());

	    return response;
	}
}
