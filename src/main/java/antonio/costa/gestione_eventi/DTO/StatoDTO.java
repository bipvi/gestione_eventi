package antonio.costa.gestione_eventi.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record StatoDTO (
        @NotEmpty(message = "Il ruolo deve essere specificato")
        @Pattern(regexp = " AL_COMPLETO|PRENOTABILE|TERMINATO", message = "Il ruolo deve essere 'AL_COMPLETO', 'PRENOTABILE' o 'TERMINATO'")
        String stato
) {
}
