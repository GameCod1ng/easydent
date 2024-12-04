package webproject.easydent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webproject.easydent.entities.Dentistry;
import webproject.easydent.entities.Reservation;
import webproject.easydent.entities.User;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByCustomerName(String customerName);

    //자주 간 병원 조회 사이즈 조회해서 내림차순으로 보여주면 되지 않을까?
    List<Reservation> findByUserAndDentistry(User user, Dentistry dentistry);
    
    // 이 병원에 이 시간에 얼만큼 예약을 했는지 해서 치과의 의사 수와 비교해서 예약시간을 블록처리 되게 30분 간격으로
    List<Reservation> findByDentistryAndReservedTime(Dentistry dentistry, String reservedTime);


}
