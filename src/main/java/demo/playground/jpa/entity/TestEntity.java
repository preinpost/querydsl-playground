package demo.playground.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "querydsl_test_table")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
}
