package webproject.easydent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.easydent.entities.Dentistry;
import webproject.easydent.entities.Reservation;

import java.util.List;

@Repository
public interface DentistryRepository extends JpaRepository<Dentistry, String> {

    List<Dentistry> findByClinicNameContaining(String clinicName);
    List<Dentistry> findByCategoryContaining(String category);
    List<Dentistry> findByAddressContaining(String address);
}
