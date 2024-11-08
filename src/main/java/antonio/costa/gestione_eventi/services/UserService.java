package antonio.costa.gestione_eventi.services;

import antonio.costa.gestione_eventi.DTO.NewUserDTO;
import antonio.costa.gestione_eventi.DTO.RuoloDTO;
import antonio.costa.gestione_eventi.entities.Evento;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.entities.enums.Ruolo;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.exceptions.NotFoundException;
import antonio.costa.gestione_eventi.repositories.UserRepo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public User saveUser(NewUserDTO body) {
        this.userRepo.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("Email "+ body.email() +" already exists");
                }
        );
        User user = new User(body.nome(), body.cognome(), body.email(), encoder.encode(body.password()));
        return this.userRepo.save(user);
    }

    public Page<User> findAllUsers(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userRepo.findAll(pageable);
    }

    public User findById(String id) {
        UUID uuid = UUID.fromString(id);
        return this.userRepo.findById(uuid).orElseThrow(() -> new NotFoundException(id));
    }

    public User findUserByEmail(String email) {
        return this.userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public User findAndUpdateUser(String userId, NewUserDTO body) {
        User user = this.findById(userId);
        if(!user.getEmail().equals(body.email())){
            this.userRepo.findByEmail(body.email()).ifPresent(
                    u -> {
                        throw new BadRequestException("Email "+ body.email() +" already exists");
                    }
            );
        }
        user.setNome(body.nome());
        user.setCognome(body.cognome());
        user.setPassword(encoder.encode(body.password()));
        user.setEmail(body.email());
        return this.userRepo.save(user);
    }

    public void deleteUser(String userId) {
        User f = this.findById(userId);
        this.userRepo.delete(f);
    }

    public User updateRuolo(String userId, RuoloDTO body) {
        User user = this.findById(userId);
        user.setRuolo(Ruolo.valueOf(body.ruolo()));
        return this.userRepo.save(user);
    }

    public List<Evento> findEventiByUSer(String userId) {
        User found = this.findById(userId);
        return found.getEventi();
    }

}