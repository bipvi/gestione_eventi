package antonio.costa.gestione_eventi.entities;

import antonio.costa.gestione_eventi.entities.enums.Ruolo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "utenti")
@JsonIgnoreProperties({"password", "role", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "authorities", "enabled"})
public class User implements UserDetails{
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String cognome;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;
    @ManyToMany
    @JoinTable(
            name = "prenotazioni",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "evento_id")
    )
    private List<Evento> eventi;

    public User(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = Ruolo.PARTECIPANTE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRuolo().name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
}
