package com.niloy.modal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String mrpPrice;

    private String sellingPrice;

    private String discountPercentage;

    private String quantity;

    private String color;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    private int numRatings;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Seller seller;

    private LocalDateTime createdAt;

    private String sizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviews> reviews = new ArrayList<>();




}
