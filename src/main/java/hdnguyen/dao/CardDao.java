package hdnguyen.dao;

import hdnguyen.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardDao extends JpaRepository<Card, Integer> {
}
