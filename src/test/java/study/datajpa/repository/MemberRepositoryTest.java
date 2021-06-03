package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        // given
        Member member = new Member("member");
        Member saveMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        // then
        assertThat(saveMember.getId()).isEqualTo(findMember.getId());
        assertThat(saveMember.getUsername()).isEqualTo(findMember.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =
                memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findUser("AAA", 10);

        // then
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<String> usernameList = memberRepository.findUsernameList();

        // then
        for (String s : usernameList) {
            System.out.println(s);
        }
    }

    @Test
    public void findMemberDto() {
        // given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);
        // when
        List<MemberDto> memberDto = memberRepository.findMemberDto();

        // then
        for (MemberDto dto : memberDto) {
            System.out.println(dto);
        }
    }

    @Test
    public void findByNames() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        // then
        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    public void returnType() {
        // given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // when
        Member findMember = memberRepository.findMemberByUsername("AAA");
        List<Member> list = memberRepository.findListByUsername("AAA");
        Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");

        // then
        System.out.println(findMember);
        for (Member member : list) {
            System.out.println(member);
        }
        System.out.println(optional);

    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElement = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        List<Member> result = memberRepository.findByUsername("member5");
        System.out.println(result.get(0));

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member1", 20, teamB));
        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void queryHint() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.changeUsername("member2");
        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> member11 = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1");
        memberRepository.save(member); // prePersist

        Thread.sleep(100);
        member.changeUsername("member2");

        em.flush(); //PreUpdate
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println(findMember.getCreateDate());
        System.out.println(findMember.getUpdateDate());
        System.out.println(findMember.getCreateBy());
        System.out.println(findMember.getLastModifiedBy());
    }

    @Test
    public void testExample() {
        Member m1 = new Member("m1", 0);
        Member m2 = new Member("m2", 0);
        Team team = new Team("teamA");
        m1.changeTeam(team);
        em.persist(m1);
        em.persist(m2);
        em.persist(team);

        em.flush();
        em.clear();

        Member member = new Member("m1");
        member.changeTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        assertThat(result.get(0).getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void testProjections() {
        Team teamA = new Team("TeamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnlyDto> result = memberRepository.findProjectionByUsername("m1", UsernameOnlyDto.class);
        for (UsernameOnlyDto usernameOnly : result) {
            System.out.println(usernameOnly.getUsername());
        }
    }
}