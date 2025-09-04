package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
public class Vote {
    private Long id;
    private Instant publishedAt;
    private Long userId;       // Who voted
    private Long pollId;       // Which poll
    private Long voteOptionId; // Which option
}