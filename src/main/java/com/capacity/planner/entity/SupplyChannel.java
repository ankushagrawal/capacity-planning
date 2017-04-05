package com.capacity.planner.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@TableGenerator(name = "idGen",table = "ID_GEN")
@Entity
@Table(name = "supply_channels")
@Data
public class SupplyChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;


    @Column(name = "supply_channel")
    @JsonProperty
    private String supplyChannel;
}
