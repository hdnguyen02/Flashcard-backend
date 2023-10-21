package hdnguyen.dao;

import hdnguyen.entity.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface DeskDao extends JpaRepository<Desk, Integer> {
    @Query("SELECT COUNT(d) > 0 FROM Desk d WHERE d.id = :deskId AND d.user.email = :email")
    boolean existDeskWithEmail(@Param("deskId") int deskId, @Param("email") String email);
}
