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

    @Value("${email.password-recover.uri=http://meusite.com.br/recover-password/}")
    private String uri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createRecoverToken(RequestTokenDTO dto){
        // pelo email -> bsucar usuário -> gerar token -> salvar no BD

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null){
            throw new ResourceNotFound("Email não exsite em nosso sistema.");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setToken(token);
        passwordRecover.setEmail(user.getEmail());
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes*60L));

        passwordRecoverRepository.save(passwordRecover);

        String body = "Acesse o link para definir uma nova senha. (Válido por " + tokenMinutes + ")"
                + "\n\n" + uri + "\n" + token;

        emailService.sendMail(new EmailDTO(user.getEmail(), "Recuperação de senha", body));

    }

    public void saveNewPassword(NewPasswordDTO dto){
        List<PasswordRecover> list = passwordRecoverRepository.searchValidToken(dto.getToken(), Instant.now());

        if (list.isEmpty()){
            throw new ResourceNotFound("Token not found");
        }

        User user = userRepository.findByEmail(list.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

    }
}