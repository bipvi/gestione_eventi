package antonio.costa.gestione_eventi.services;

import antonio.costa.gestione_eventi.DTO.NewEventoDTO;
import antonio.costa.gestione_eventi.DTO.StatoDTO;
import antonio.costa.gestione_eventi.entities.Evento;
import antonio.costa.gestione_eventi.entities.User;
import antonio.costa.gestione_eventi.entities.enums.Stato;
import antonio.costa.gestione_eventi.exceptions.BadRequestException;
import antonio.costa.gestione_eventi.exceptions.NotFoundException;
import antonio.costa.gestione_eventi.repositories.EventoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EventoService {
    @Autowired
    private EventoRepo eventoRepo;
    @Autowired
    private UserService userService;

    public Evento saveEvento(NewEventoDTO body) {
        Evento evento = new Evento(body.titolo(), body.descrizione(), body.data(), body.num_max_partecipanti());
        return this.eventoRepo.save(evento);
    }

    public Evento findById(int id) {
        return this.eventoRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Evento> findAllEventi(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        return this.eventoRepo.findAll(pageable);
    }

    public Evento findAndAddPartecipante(int eventoId, int partecipantiId) {
        Evento found = this.findById(eventoId);
        User foundUser = this.userService.findById(partecipantiId);
        if (found.getStato() == Stato.AL_COMPLETO) {
            throw new BadRequestException("L'evento " + found.getTitolo() + " è al completo");
        }
        if ((long) found.getPartecipanti().size() >= found.getNum_max_partecipanti()){
            throw new BadRequestException("L'evento " + found.getTitolo() + " è al completo");
        }
        if (found.getPartecipanti().stream().anyMatch(p -> p.equals(foundUser))) {
            throw new BadRequestException("Partecipante " + foundUser.getNome() + " si è già prenotato per l'evento");
        }
        if (found.getNum_max_partecipanti() == (long) found.getPartecipanti().size() + 1){
            found.setStato(Stato.AL_COMPLETO);
        }
        List<User> partecipanti = found.getPartecipanti();
        partecipanti.add(foundUser);
        found.setPartecipanti(partecipanti);
        return this.eventoRepo.save(found);
    }

    public Evento findAndUpdate(int eventoId, NewEventoDTO body) {
        Evento eventoFound = this.findById(eventoId);
        eventoFound.setDescrizione(body.descrizione());
        eventoFound.setData(body.data());
        eventoFound.setNum_max_partecipanti(body.num_max_partecipanti());
        eventoFound.setTitolo(body.titolo());
        return this.eventoRepo.save(eventoFound);
    }

    public Evento updateState(int eventoId, StatoDTO body) {
        Evento found = this.findById(eventoId);
        found.setStato(Stato.valueOf(body.stato()));
        return this.eventoRepo.save(found);
    }

}
