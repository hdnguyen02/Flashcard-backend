package hdnguyen.dao;

import hdnguyen.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface HistoryDao extends JpaRepository<History, Integer> {
    Optional<History> findByIdDeckAndDate(Integer idDeck, String date);
}
