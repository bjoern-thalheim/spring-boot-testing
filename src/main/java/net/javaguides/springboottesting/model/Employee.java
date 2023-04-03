package net.javaguides.springboottesting.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="first_name", nullable = false)
    private String lastName;
    @Column(name="last_name", nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String email;
}
