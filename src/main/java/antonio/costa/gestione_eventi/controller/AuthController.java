package antonio.costa.gestione_eventi.controller;

import antonio.costa.gestione_eventi.DTO.LoginResponse;
import antonio.costa.gestione_eventi.DTO.NewLoginDTO;
import antonio.costa.gestione_eventi.DTO.NewUserDTO;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.services.AuthService;
import antonio.costa.gestione_eventi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponse login (@RequestBody NewLoginDTO body){
        return new LoginResponse(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @GetMapping
    public Page<User> findAll(@RequestParam (defaultValue = "0") int page,
                                    @RequestParam (defaultValue = "10") int size,
                                    @RequestParam (defaultValue = "email") String sortBy) {
        return this.userService.findAllUsers(page,size,sortBy);
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User save (@RequestBody @Validated NewUserDTO body, BindingResult result){
        if(result.hasErrors()){
            String error = result.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + error);
        }
        return this.userService.saveUser(body);
    }
}
