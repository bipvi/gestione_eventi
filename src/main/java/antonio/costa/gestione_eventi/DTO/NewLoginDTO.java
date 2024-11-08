package antonio.costa.gestione_eventi.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewLoginDTO(@NotEmpty(message = "Il campo email non può essere vuioto!")
                          @Email(message = "L'email deve essere del tipo user@example.com")
                          String email,
                          @NotEmpty@Size(min = 6, max=20, message = "La password deve essere maggiore di 6 caratteri e minore di 20 caratteri")
                          String password){
}
