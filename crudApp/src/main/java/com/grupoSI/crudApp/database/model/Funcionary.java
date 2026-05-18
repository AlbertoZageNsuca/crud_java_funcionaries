package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import java.time.LocalDate;

@Entity
@Table(name="funcionaries")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SoftDelete(columnName = "deleted")
public class Funcionary {
    //campo para ID
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //campos para nome,localizacao, profissao e dataNascimento
    @Column(nullable = false)
    private String name;
    private String localization;
    private String ocupation;
    private LocalDate birthDate;


    @Column(nullable = false, unique = true)
    private String email;
    private int phoneNumber;

    //campo pra imagem de perfil
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte profileImg;

    //campo para salario
    @Column(nullable = false)
    private double salary;

    //campo departamento/area
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "departament_id", nullable = false)
    private Departament departament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;


}
