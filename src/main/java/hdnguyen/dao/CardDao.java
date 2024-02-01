package hdnguyen.dao;

import hdnguyen.entity.ECard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardDao extends JpaRepository<ECard, String> {

}
