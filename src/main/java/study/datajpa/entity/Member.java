package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 만들기, 기본생성자는 JPA 스펙으로 private는 안된다.
@ToString(of = {"id", "username", "age"}) //toString을 만들 때에는 가급적이면 연관관계객체는 넣지 않는것이 좋다(서로 부르면서 무한루프 된다.)
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username =: username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) { this.username = username; }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
