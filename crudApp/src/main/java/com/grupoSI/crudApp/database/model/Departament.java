package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departaments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O nome do departamento é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "departament",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Funcionary> funcionaryList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Funcionary> getFuncionaryList() {
        return funcionaryList;
    }

    public void setFuncionaryList(List<Funcionary> funcionaryList) {
        this.funcionaryList = funcionaryList;
    }
}
