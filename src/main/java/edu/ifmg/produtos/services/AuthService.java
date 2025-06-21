package edu.ifmg.produtos.services;

import edu.ifmg.produtos.dto.EmailDTO;
import edu.ifmg.produtos.dto.NewPasswordDTO;
import edu.ifmg.produtos.dto.RequestTokenDTO;
import edu.ifmg.produtos.entities.PasswordRecover;
import edu.ifmg.produtos.entities.User;
import edu.ifmg.produtos.repositories.PasswordRecoverRepository;
import edu.ifmg.produtos.repositories.UserRepository;
import edu.ifmg.produtos.services.exceptions.ResourceNotFound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private int tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String uri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createRecorverToken(RequestTokenDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if(user == null) {
            throw new ResourceNotFound("Usuário não encontrado com o email: " + dto.getEmail());
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();

        passwordRecover.setToken(token);
        passwordRecover.setEmail(user.getEmail());
        passwordRecover.setExpiration(
                java.time.Instant.now().plusSeconds(tokenMinutes * 60)
        );

        passwordRecoverRepository.save(passwordRecover);

        String body = "Acesse o link para definir uma nova senha: "
                + uri
                + token
                + " Validos por " + tokenMinutes + " minutos.";

        emailService.sendMail(
                new EmailDTO(
                        token,
                        user.getEmail(),
                        "Recuperação de senha",
                        body
                )
        );
    }

    public void saveNewPassword(NewPasswordDTO dto) {
        List<PasswordRecover> list = passwordRecoverRepository.searchValidToken(dto.getToken(), Instant.now());

        if (list.isEmpty()) {
            throw new ResourceNotFound("Token inválido ou expirado.");
        }

        PasswordRecover passwordRecover = list.get(0); // ✅ acesso correto ao primeiro item
        User user = userRepository.findByEmail(passwordRecover.getEmail());

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user); // ❗ não se esqueça de salvar a nova senha
    }
}