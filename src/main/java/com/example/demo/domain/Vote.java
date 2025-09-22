package com.example.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Data
@NoArgsConstructor
@Entity

public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant publishedAt;

    @ManyToOne
    private User voter; // Who voted

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Poll poll; // Which poll

    @ManyToOne
    private VoteOption votesOn; // Which option
}