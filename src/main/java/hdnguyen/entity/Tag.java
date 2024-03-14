package hdnguyen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
     @Id
     @Column(length = 36)
     private String id;

     @Column(nullable = false, length =  50)
     private String name;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name="uid")
     private User user;

     @ManyToMany(fetch = FetchType.EAGER)
     @JoinTable( name = "card_tag", joinColumns = @JoinColumn(name = "id_tag"),inverseJoinColumns = @JoinColumn(name = "id_card"))
     private List<Card> cards;
}
