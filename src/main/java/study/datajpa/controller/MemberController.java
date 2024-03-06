package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> findMembers = memberRepository.findAll(pageable);
        //Page<MemberDto> membersDto = findMembers.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        //Page<MemberDto> memberDto = findMembers.map(m -> new MemberDto(m));
        Page<MemberDto> membersDto = findMembers.map(MemberDto::new); //람다식 변환
        return membersDto;
    }

    //@PostConstruct
    public void init(){
        for(int i = 0; i < 100 ; i++){
            memberRepository.save(new Member("user" + i , i ));
        }
    }
}
