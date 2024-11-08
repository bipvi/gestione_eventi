package antonio.costa.gestione_eventi.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotEmpty(message = "Il campo nome è obbligatorio")
        String nome,
        @NotEmpty(message = "Il campo cognome è obbligatorio")
        String cognome,
        @NotEmpty(message = "Il campo email è obbligatorio")
        @Email(message = "L'email deve rispettare il formato : mario@rossi.it")
        String email,
        @NotEmpty(message = "Il campo password è obbligatorio")
        @Size(min = 4,message = "La password deve contenere almeno 4 caratteri!")
        String password
) {
}
