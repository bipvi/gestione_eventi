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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/{userId}")
    public User findById(@PathVariable("userId") String userId){
        return this.userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public User update(@PathVariable("userId") String userId, @RequestBody @Validated NewUserDTO body, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String messasge = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" - "));
            throw new BadRequestException(messasge);
        }
        return this.userService.findAndUpdateUser(userId, body);
    }

    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated NewUserDTO body) {
        return this.userService.findAndUpdateUser(currentAuthenticatedUser.getId().toString(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.userService.deleteUser(currentAuthenticatedUser.getId().toString());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") String id) {
        this.userService.deleteUser(id);
    }

    @PatchMapping("/{userId}/stato")
    public User uploadImage(@PathVariable("userId") String id, @RequestParam("stato") @Validated RuoloDTO ruolo) {
        return this.userService.updateRuolo(id,ruolo);
    }

    @GetMapping("/{userId}/eventi")
    public List<Evento> getEventi(@PathVariable("userId") String id) {
        return this.userService.findEventiByUSer(id);
    }

    @PatchMapping("/me/eventoId")
    public Evento prenotaEvento(@PathVariable("userId") String id, @RequestParam("eventoId") String eventoId ) {
        return this.eventoService.findAndAddPartecipante(id, eventoId);
    }

}
