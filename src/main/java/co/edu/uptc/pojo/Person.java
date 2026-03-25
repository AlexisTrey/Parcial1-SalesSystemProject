package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Person {
    private int id;
    private String name;
    private String lastName;
    private String gender;
    private LocalDate birthdate;
}
