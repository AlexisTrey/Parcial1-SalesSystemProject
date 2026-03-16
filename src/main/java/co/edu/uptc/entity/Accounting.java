package co.edu.uptc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Accounting {
    private String description;
    private String type;
    private double amount;
    private LocalDateTime dateTime;
}
