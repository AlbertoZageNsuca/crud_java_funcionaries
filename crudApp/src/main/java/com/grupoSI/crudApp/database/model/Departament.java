package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="departaments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Departament {

    //campo para ID do departamento
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //campo para nome do departamento
    @Column(nullable = false)
    private String name;

    //UM departamento tem MUITOS funcionários(campo pra funcionarios)
    @OneToMany(mappedBy = "departament",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY)
    private List<Funcionary> funcionaryList= new ArrayList<>();

}

