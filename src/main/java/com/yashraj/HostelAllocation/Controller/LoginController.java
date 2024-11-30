package com.yashraj.HostelAllocation.Controller;

import com.yashraj.HostelAllocation.DTO.LoginDTO;
import com.yashraj.HostelAllocation.Repository.StudentRepository;
import com.yashraj.HostelAllocation.helper.EncryptPassword;
import com.yashraj.HostelAllocation.helper.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {

    private final JWTHelper jwtHelper;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    public EncryptPassword encryptPassword;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public LoginController(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception {
        String who = loginDTO.getWho();
        String username = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        if (who.equals("admin")) {
            String user = "admin@iiitb.ac.in";
            String pass = "$2a$10$zl/grlqw8ERKwYoIBVDk3.0AlxyQ/NtDf8M4twiHy2tJ/3d/NknOq";
            // Admin@123

            boolean valid = passwordEncoder.matches(password, pass) && user.equals(username);
            if (valid) {
                String token = jwtHelper.generateToken(loginDTO.getEmail());
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(500).body("There is an Exception.");
            }
        }

        return ResponseEntity.status(400).body("Invalid user type.");
    }

}
