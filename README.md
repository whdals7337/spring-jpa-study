# Spring-Jpa-Study

## 실전! 스프링 데이터 JPA
실전! 스프링 데이터 JPA: https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84


1. 네임드 쿼리는 애플리케이션 로딩 시점에 오타를 잡을수 있음
2. Criteria 사용하기 복잡, 가독성 낮음
3. Query By Example outer 조인 안됨
4. Projections - 프로젝션 대상이 root 엔티티면 유용
5. 네이티브 SQL을 DTO로 조회할 때는 JdbcTemplate or myBatis 권장
   - 네이티브 + 프로젝션으로 정적쿼리에 경우 고려 해볼만함