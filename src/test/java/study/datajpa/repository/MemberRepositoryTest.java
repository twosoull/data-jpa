package study.datajpa.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        Assertions.assertThat(findMember).isEqualTo(savedMember);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();


    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findByUsername("AAA");
        Member findMember = findMembers.get(0);
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers = memberRepository.findUser("AAA",10);
        Member findMember = findMembers.get(0);
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> findMembers = memberRepository.findUsernameList();

        for (String s : findMembers) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member member : result) {
            System.out.println("member = " + member);
        }

    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Optional<Member> result = memberRepository.findOptionalByUsername("sd");
        System.out.println("result = " + result);

    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1" , 10));
        memberRepository.save(new Member("member2" , 10));
        memberRepository.save(new Member("member3" , 10));
        memberRepository.save(new Member("member4" , 10));
        memberRepository.save(new Member("member5" , 10));

        int age =10 ;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest); //반환 타입을 Page로 받으면 totalCount 쿼리도 함께 날린다.
        //Slice<Member> page = memberRepository.findByAge(age, pageRequest); //리미트를 3으로 주지만 +1 개를 해서 가져온다. 더보기를 클릭할 때를 대비해서 만들어졌다. 또 그냥 List로 만들어도 된다.

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null)); //얘는 반환해도 된다.

        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0); //페이지 넘버도 가져올 수 있다....
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); //
        Assertions.assertThat(page.isFirst()).isTrue(); // 첫페이지인지
        Assertions.assertThat(page.hasNext()).isTrue(); // 다음페이지가 있는지

    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1" , 10));
        memberRepository.save(new Member("member2" , 19));
        memberRepository.save(new Member("member3" , 20));
        memberRepository.save(new Member("member4" , 21));
        memberRepository.save(new Member("member5" , 40));

        //where
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraph2ByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername = " + member.getUsername());
            System.out.println("member.getTeam = " + member.getTeam());
            System.out.println("member.getTeam().getName" + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");

        findMember.setUsername("member2");

        em.flush();

    }

    @Test
    public void lock(){
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findLockByUsername("member1");

        findMember.setUsername("member2");

        em.flush();

    }

    @Test
    public void callCustom(){
        memberRepository.findMemberCustom();

    }

    @Test
    public void specBasic(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List result = memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}