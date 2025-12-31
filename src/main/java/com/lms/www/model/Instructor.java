package com.lms.www.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
