package webproject.easydent.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import webproject.easydent.review.comment.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "users")
public class User{
    @Column(name = "user_id")
    @Id
    String email;

    String name;

    String phoneNumber;

    LocalDate birthDay;

    String accountType; // 어떤 계정(구글, 카카오)으로 로그인 했는지

    LocalDate createdAt; // 계정 생성일

    String address; // 주소


    // FamilyAccount 관계 수정
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "family_id")
    private FamilyAccount familyAccount;

    private Boolean isFamilyLeader; // 가족 리더인지 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Location> locationList; // 리뷰에 대한 답글 리스트

}
