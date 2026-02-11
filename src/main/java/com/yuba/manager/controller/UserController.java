package com.yuba.manager.controller;

import com.yuba.manager.entity.User;
import com.yuba.manager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("activePage", "users");
        return "users";
    }

    @PostMapping("/add")
    public String add(@RequestParam String username, 
                      @RequestParam String password,
                      @RequestParam String role) {
        
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/users?error=Username already exists";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        
        userRepository.save(user);
        return "redirect:/users";
    }
    
    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        // Prevent deleting admin? Maybe not strictly required for this demo but good practice
        // For now, simple delete
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
