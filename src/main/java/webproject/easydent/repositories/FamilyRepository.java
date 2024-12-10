package webproject.easydent.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.easydent.entities.FamilyAccount;
import webproject.easydent.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends JpaRepository<FamilyAccount, Long> {
    List<FamilyAccount> findByLeader(User leader);
    List<FamilyAccount> findByMember(User member);

    Optional<FamilyAccount> findByLeaderOrMember(User leader, User member);
}
