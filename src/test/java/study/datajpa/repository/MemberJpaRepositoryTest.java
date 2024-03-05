package study.datajpa.repository;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void TestNamedQuery(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("bbb",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByusername("AAA");
        Member findMember = result.get(0);

        Assertions.assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void paging() {
        memberJpaRepository.save(new Member("member1" , 10));
        memberJpaRepository.save(new Member("member2" , 10));
        memberJpaRepository.save(new Member("member3" , 10));
        memberJpaRepository.save(new Member("member4" , 10));
        memberJpaRepository.save(new Member("member5" , 10));
        
        int age = 10;
        int offset = 1;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        Long totalCount = memberJpaRepository.totalCount(age);

        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate(){
        memberJpaRepository.save(new Member("member1" , 10));
        memberJpaRepository.save(new Member("member2" , 19));
        memberJpaRepository.save(new Member("member3" , 20));
        memberJpaRepository.save(new Member("member4" , 21));
        memberJpaRepository.save(new Member("member5" , 40));

        //where
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }
}