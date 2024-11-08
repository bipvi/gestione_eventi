package antonio.costa.gestione_eventi.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record NewEventoDTO (
        @NotEmpty(message = "Il campo titolo è obbligatorio")
        String titolo,
        String descrizione,
        @NotEmpty(message = "Il campo data è obbligatorio")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "Il campo deve essere una data nel formato 'yyyy-mm-dd'")
        LocalDate data,
        @NotNull(message = "Il campo numero massimmo partecipanti è obbligatorio")
        int num_max_partecipanti
) {
}
