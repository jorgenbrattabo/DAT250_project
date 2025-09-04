package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class Poll {
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private Long userId; // Creator
    private List<Long> voteOptionIds; // IDs of options for this poll
}