package hdnguyen.dao;

import hdnguyen.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface DeckDao extends JpaRepository<Deck, String> {
    @Query("SELECT COUNT(d) > 0 FROM Deck d WHERE d.id = :idDeck AND d.user.uid = :uid")
    boolean existDeckWithEmail(@Param("idDeck") String idDeck, @Param("uid") String uid);
}
