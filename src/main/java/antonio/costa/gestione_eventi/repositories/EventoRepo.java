package antonio.costa.gestione_eventi.repositories;

import antonio.costa.gestione_eventi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoRepo extends JpaRepository<Evento, UUID> {
}
