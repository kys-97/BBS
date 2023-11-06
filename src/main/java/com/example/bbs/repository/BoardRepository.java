package com.example.bbs.repository;

import com.example.bbs.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//JpaRepository 인터페이스를 상속
//JpaRepository를 상속할 때는 제네릭스 타입으로 <Question, Integer> 처럼 리포지터리의 대상이 되는 엔티티의 타입(Question)과 해당 엔티티의 PK의 속성 타입(Integer)을 지정
public interface BoardRepository extends JpaRepository<Board, Integer> {
    //제목 조회
    Board findByTitle(String title);
    //제목, 내용
    Board findByTitleAndContent(String title, String content);
    //특정 문자열 포함
    List<Board> findByTitleAndContentLike(String title, String content);
    //paging
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAll(Specification<Board> spec, Pageable pageable);

    @Query("select "
            + "distinct b "
            + "from Board b "
            + "left outer join SiteUser u1 on b.author=u1 "
            + "left outer join Comment c on c.board=b "
            + "left outer join SiteUser u2 on c.author=u2 "
            + "where "
            + "   b.title like %:kw% "
            + "   or b.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or c.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
