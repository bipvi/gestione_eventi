package antonio.costa.gestione_eventi.services;

import antonio.costa.gestione_eventi.DTO.NewLoginDTO;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.exceptions.UnauthorizedException;
import antonio.costa.gestione_eventi.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWT jwt;

    public String checkCredentialsAndGenerateToken(NewLoginDTO body) {
        User found = this.userService.findUserByEmail(body.email());
        System.out.println(found);
        if(bcrypt.matches(body.password(), found.getPassword())) {
            return jwt.generateToken(found);
        } else throw new UnauthorizedException("Credenziali errate");
    }
}
