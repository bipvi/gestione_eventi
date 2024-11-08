package antonio.costa.gestione_eventi.controller;

import antonio.costa.gestione_eventi.DTO.NewEventoDTO;
import antonio.costa.gestione_eventi.DTO.StatoDTO;
import antonio.costa.gestione_eventi.entities.Evento;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping("/{eventoID}")
    public Evento findById(@PathVariable("eventoID") int eventoID) {
        return eventoService.findById(eventoID);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evento save(@RequestBody@Validated NewEventoDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" - "));
            throw new BadRequestException(error);
        }
        return this.eventoService.saveEvento(body);
    }

    @PutMapping("/{eventoId}")
    public Evento updateEvento(@PathVariable("eventoId") int id,@RequestBody NewEventoDTO body , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String messasge = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" - "));
            throw new BadRequestException(messasge);
        }
        return this.eventoService.findAndUpdate(id, body);
    }

    @PatchMapping("/{eventoId}/stato")
    public Evento updateStato(@PathVariable("eventoId") int dipendenteId, @RequestParam("stato") StatoDTO stato) {
        return this.eventoService.updateState(dipendenteId, stato);
    }

    @GetMapping
    public Page<Evento> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "nome") String sortBy){
        return this.eventoService.findAllEventi(page, size, sortBy);
    }

}
