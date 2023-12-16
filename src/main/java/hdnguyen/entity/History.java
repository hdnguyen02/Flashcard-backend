package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "id_deck")
    private Integer idDeck;
    private String date;
    @Column(name = "total_studied_new")
    private Integer totalStudiedNew;
    @Column(name = "total_studied_review")
    private Integer totalStudiedReview;
}
