package antonio.costa.gestione_eventi.controller;

import antonio.costa.gestione_eventi.DTO.NewUserDTO;
import antonio.costa.gestione_eventi.DTO.RuoloDTO;
import antonio.costa.gestione_eventi.entities.Evento;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.services.EventoService;
import antonio.costa.gestione_eventi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "nome") String sortBy){
        return this.userService.findAllUsers(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody NewUserDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(" - "));
            throw new BadRequestException(error);
        }
        return this.userService.saveUser(body);
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable("userId") int userId){
        return this.userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public User update(@PathVariable("userId") int userId, @RequestBody @Validated NewUserDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String messasge = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" - "));
            throw new BadRequestException(messasge);
        }
        return this.userService.findAndUpdateUser(userId, body);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") int id) {
        this.userService.deleteUser(id);
    }

    @PatchMapping("/{userId}/stato")
    public User uploadImage(@PathVariable("userId") int id, @RequestParam("stato") @Validated RuoloDTO ruolo) {
        return this.userService.updateRuolo(id,ruolo);
    }

    @GetMapping("/{userId}/eventi")
    public List<Evento> getEventi(@PathVariable("userId") int id) {
        return this.userService.findEventiByUSer(id);
    }

    @PatchMapping("/{userId}/eventoId")
    public Evento prenotaEvento(@PathVariable("userId") int id, @RequestParam("eventoId") int eventoId ) {
        return this.eventoService.findAndAddPartecipante(id, eventoId);
    }

}
