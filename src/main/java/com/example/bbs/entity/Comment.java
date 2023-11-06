package com.example.bbs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne //하나의 질문에 여러 개의 답이 달릴 수 있기 때문에 n:1 관계
    private Board board;

    @ManyToOne
    private SiteUser author;
    private LocalDateTime modifyDate;

    //좋아요 속성
    @ManyToMany //게시물과 좋아요는 대등한 관계
    Set<SiteUser> like;

}
