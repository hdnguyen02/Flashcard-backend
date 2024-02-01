package hdnguyen.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "id_deck",  nullable = false)
    private String idDeck;

    @Column(nullable = false, length = 10)
    private String date;

    @Column(name = "total_studied_new", nullable = false)
    private Integer totalStudiedNew;

    @Column(name = "total_studied_review", nullable = false)
    private Integer totalStudiedReview;
}
