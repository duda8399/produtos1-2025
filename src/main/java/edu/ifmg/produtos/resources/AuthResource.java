package edu.ifmg.produtos.resources;

import edu.ifmg.produtos.dto.NewPasswordDTO;
import edu.ifmg.produtos.dto.RequestTokenDTO;
import edu.ifmg.produtos.services.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @PostMapping("/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody RequestTokenDTO dto){
        authService.createRecoverToken(dto);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/new-password")
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto){

        authService.saveNewPassword(dto);

        return ResponseEntity.noContent().build();

    }
}