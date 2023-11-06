package com.example.bbs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}