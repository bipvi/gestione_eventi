package antonio.costa.gestione_eventi.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record RuoloDTO (
        @NotEmpty(message = "Il ruolo deve essere specificato")
        @Pattern(regexp = "GESTORE_EVENTI|PARTECIPANTE|ADMIN", message = "Il ruolo deve essere 'PARTECIPANTE', 'GESTORE_EVENTI' o 'ADMIN'")
        String ruolo
) {
}
