package com.capacity.planner.entity;

import com.capacity.planner.enums.UsecaseEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name = "usecases")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class Usecase {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    @JsonProperty
    private UsecaseEnum usecase;
}
