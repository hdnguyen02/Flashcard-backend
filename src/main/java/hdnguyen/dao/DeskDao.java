package hdnguyen.dao;

import hdnguyen.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DeskDao extends JpaRepository<Desk, Integer> {



}
