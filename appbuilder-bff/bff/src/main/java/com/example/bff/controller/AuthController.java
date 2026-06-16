package com.example.bff.controller;

import com.example.bff.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/session")
    @ResponseBody
    public ResponseEntity<?> session(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok("Usuário autenticado: " + principal.toString());
        }
        return ResponseEntity.status(401).body("Nenhuma sessão ativa");
    }

    @GetMapping("/redirect")
    public String redirectToKeycloak() {
        return "redirect:/oauth2/authorization/keycloak";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        System.out.println("Entreiiiii");
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.ok("Sessão encerrada.");
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        if (request.getSession(false) != null) {
//            request.getSession().invalidate();
//        }
//        SecurityContextHolder.clearContext();
//
//        String redirectUri = "http://localhost:5173"; // ou http://localhost:5173/logout-sucesso
//        return "redirect:http://localhost:8080/keycloak/realms/Appbuilder/protocol/openid-connect/logout?redirect_uri=" + redirectUri;
//    }
}
