package com.example.AuthService.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.AuthService.payload.request.LoginRequest;
import com.example.AuthService.payload.request.SignupRequest;
import com.example.AuthService.payload.response.JwtResponse;
import com.example.AuthService.payload.response.MessageResponse;
import com.example.AuthService.security.jwt.JwtUtils;
import com.example.AuthService.security.services.UserDetailsImpl;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

import model.ERole;
import model.Role;
import model.User;

@Service("Auth Service")
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

	}

	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			logger.error("Roles can not be null!");
		} else {
			strRoles.forEach(role -> {
				if (role.equals("admin")) {
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
					roles.add(adminRole);
				} else if (role.equals("user")) {
					Role userRole = roleRepository.findByName(ERole.ROLE_USER);
					roles.add(userRole);
				} else {
					logger.error("Unknown role : " + role.toString());
				}
			});

			if (!roles.isEmpty()) {
				User user = User.builder().email(signUpRequest.getEmail())
						.password(encoder.encode(signUpRequest.getPassword())).username(signUpRequest.getUsername())
						.roles(roles).id(userRepository.getMaxId() + 1).build();
				userRepository.save(user);
				return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
			}
		}
		return ResponseEntity.ok(new MessageResponse("Registration failed!"));
	}
}
