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

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(byte profileImg) {
        this.profileImg = profileImg;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Departament getDepartament() {
        return departament;
    }

    public void setDepartament(Departament departament) {
        this.departament = departament;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
