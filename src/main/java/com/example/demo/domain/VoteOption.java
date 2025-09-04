package com.example.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoteOption {
    private Long id;
    private String caption;
    private int presentationOrder;
    private Long pollId; // Which poll this option belongs to
}