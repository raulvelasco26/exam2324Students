package cat.tecnocampus.notes2324.persistence;

import cat.tecnocampus.notes2324.application.dtos.UserDTO;
import cat.tecnocampus.notes2324.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
   //TODO 2.3 Uncomment the method and write the query. Note that the query must return a list of UserDTO instead of User

    //List<UserDTO> findUsersRatedByNotes();

}