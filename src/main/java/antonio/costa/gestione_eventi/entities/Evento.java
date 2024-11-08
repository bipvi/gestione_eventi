package antonio.costa.gestione_eventi.entities;

import antonio.costa.gestione_eventi.entities.enums.Stato;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue
    private UUID id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private int num_max_partecipanti;
    @Enumerated(EnumType.STRING)
    private Stato stato;
    @ManyToMany(mappedBy = "eventi")
    private List<User> partecipanti;

    public Evento(String titolo, String descrizione, LocalDate data, int num_max_partecipanti) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.num_max_partecipanti = num_max_partecipanti;
        this.stato = Stato.PRENOTABILE;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "stato=" + stato +
                ", num_max_partecipanti=" + num_max_partecipanti +
                ", descrizione='" + descrizione + '\'' +
                ", data=" + data +
                ", titolo='" + titolo + '\'' +
                ", id=" + id +
                '}';
    }
}
