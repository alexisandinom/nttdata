package com.nttdata.customerservice.persons.application.dto;

import com.nttdata.customerservice.persons.domain.Person;
import com.nttdata.customerservice.persons.infrastructure.jpa.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonRequestDTO {
    private String id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    @Length(min = 1, max = 75, message = "Name must be between 1 and 75 characters")
    private String name;

    private Gender gender;

    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Age cannot be greater than 120")
    private Integer age;

    @NotNull(message = "Identification cannot be null")
    @NotBlank(message = "Identification cannot be empty")
    @Length(min = 1, max = 13, message = "Identification must be between 1 and 13 characters")
    private String identification;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be empty")
    @Length(min = 1, max = 80, message = "Address must be between 1 and 80 characters")
    private String address;

    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be empty")
    @Length(min = 1, max = 10, message = "Phone must be between 1 and 10 characters")
    private String phone;


    public Person toPerson() {
        return Person.create(
                null,
                this.getName(),
                this.getGender(),
                this.getAge(),
                this.getIdentification(),
                this.getAddress(),
                this.getPhone());
    }
}


