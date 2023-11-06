package com.example.bbs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    //Comment 엔터티 객체로 구성된 commentList를 속성으로 추가
    //OneToMany annotation 설정
    //Board 객체에서 답변을 참조하기 위해 board.getCommentList() 호출
    //OneToMany annotation에 사용된 mappedBy는 참조 엔터티의 속성명을 의미
    //Comment entity에서 Board entity를 참조한 속성명 board를 mappedBy에 전달
    @OneToMany(mappedBy="board", cascade = CascadeType.REMOVE)
    //질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서 @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용
    private List<Comment> commentList;

    @ManyToOne //여러개의 댓글이 한 명의 사용자에게 작성
    private SiteUser author;
    private LocalDateTime modifyDate;

    //좋아요 속성
    @ManyToMany //게시물과 좋아요는 대등한 관계
    Set<SiteUser> like;
}
