package com.grupoSI.crudApp.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Informe um número de telefone válido (8 a 15 dígitos)")
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull(message = "O salário é obrigatório")
    @DecimalMin(value = "0.01", message = "O salário deve ser maior que zero")
    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departament_id", nullable = false)
    private Departament departament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
