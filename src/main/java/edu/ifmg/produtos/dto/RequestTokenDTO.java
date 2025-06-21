package edu.ifmg.produtos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RequestTokenDTO {

    @Email(message = "Email inválido")
    @NotBlank(message = "Campo obrigatório")
    private String email;

    public RequestTokenDTO() {
    }

    public RequestTokenDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RequestTokenDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}