package edu.ifmg.produtos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RequestTokenDTO {

    @NotBlank(message = "campo obrigatório")
    @Email(message = "e-mail inválido")
    public String email;

    public RequestTokenDTO() {}

    public RequestTokenDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}