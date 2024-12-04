package webproject.easydent.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String address; //경기 성남시 분당구 판교역로 166

    public String zonecode; //우편번호

    public String sigungu; //sigungu: "수성구"

    public String bname; //법정동 : 복현동

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
