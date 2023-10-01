package hdnguyen.dao;

import hdnguyen.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskDao extends JpaRepository<Desk, Integer> {



}
