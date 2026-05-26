package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDate;

@Entity
@Table(name = "funcionaries")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SoftDelete(columnName = "deleted")
public class Funcionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String name;

    @Size(max = 150, message = "A localização não pode ter mais de 150 caracteres")
    private String localization;

    @Size(max = 100, message = "A ocupação não pode ter mais de 100 caracteres")
    private String ocupation;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser uma data no passado")
    private LocalDate birthDate;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    // Removido unique=true — email pode repetir entre registos eliminados (soft delete)
    @Column(nullable = false)
    private String email;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Informe um número de telefone válido (8 a 15 dígitos)")
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull(message = "O salário é obrigatório")
    @DecimalMin(value = "100000.00", message = "O salário mínimo é de 100.000 Kz")
    private Double salary;

    @Column(length = 500)
    private String photoUrl;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departament_id", nullable = false)
    private Departament departament;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl=photoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getPhotoUrl() {
        return photoUrl;
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