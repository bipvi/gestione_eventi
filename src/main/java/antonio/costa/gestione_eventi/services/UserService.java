package antonio.costa.gestione_eventi.services;

import antonio.costa.gestione_eventi.DTO.NewUserDTO;
import antonio.costa.gestione_eventi.DTO.RuoloDTO;
import antonio.costa.gestione_eventi.entities.Evento;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.entities.enums.Ruolo;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.exceptions.NotFoundException;
import antonio.costa.gestione_eventi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User saveUser(NewUserDTO body) {
        this.userRepo.findByEmail(body.email()).ifPresent(
                user -> {
                    throw new BadRequestException("Email "+ body.email() +" already exists");
                }
        );
        User user = new User(body.nome(), body.cognome(), body.email(), body.password());
        return this.userRepo.save(user);
    }

    public Page<User> findAllUsers(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userRepo.findAll(pageable);
    }

    public User findById(int id) {
        return this.userRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findUserByEmail(String email) {
        return this.userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
    }

    public User findAndUpdateUser(int userId, NewUserDTO body) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        if(!user.getEmail().equals(body.email())){
            this.userRepo.findByEmail(body.email()).ifPresent(
                    u -> {
                        throw new BadRequestException("Email "+ body.email() +" already exists");
                    }
            );
        }
        user.setNome(body.nome());
        user.setCognome(body.cognome());
        user.setPassword(body.password());
        user.setEmail(body.email());
        return this.userRepo.save(user);
    }

    public void deleteUser(int userId) {
        User f = this.userRepo.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        this.userRepo.delete(f);
    }

    public User updateRuolo(int userId, RuoloDTO body) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        user.setRuolo(Ruolo.valueOf(body.ruolo()));
        return this.userRepo.save(user);
    }

    public List<Evento> findEventiByUSer(int userId) {
        User found = this.findById(userId);
        return found.getEventi();
    }

}