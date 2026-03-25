package co.edu.uptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private int id;
    private String description;
    private String unit;
    private double price;
}
