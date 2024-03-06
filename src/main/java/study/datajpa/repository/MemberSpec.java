package study.datajpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamName){
        return new Specification<Member>(){

            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인
                return criteriaBuilder.equal(t.get("name"), teamName);
            }
        };

    }

    public static Specification<Member> username(final String username){
        return new Specification<Member>(){

            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Member, Team> t = root.join("member", JoinType.INNER);// 회원과 조인
                return criteriaBuilder.equal(t.get("username"), username);
            }
        };
    }
}
