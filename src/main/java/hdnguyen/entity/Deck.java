package hdnguyen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Table(name="decks")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="uid")
    private User user;


    @OneToMany(mappedBy = "deck",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Card> cards;

    @Column(name = "create_at", nullable = false)
    private Date createAt;
}
