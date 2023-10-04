package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "tags")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
     private String name;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name="email_user")
     private User user;

     @ManyToMany(fetch = FetchType.EAGER)
     @JoinTable( name = "card_tag", joinColumns = @JoinColumn(name = "id_tag"),inverseJoinColumns = @JoinColumn(name = "id_card"))
     private List<Card> cards;
}
