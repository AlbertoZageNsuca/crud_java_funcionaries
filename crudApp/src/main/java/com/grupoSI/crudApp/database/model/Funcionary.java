package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="funcionaries")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Funcionary {
    //campo para
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
    private int tel;



}
