package com.capacity.planner.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
public class UsecaseWiseSupplyChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "usecase_id")
    @JsonProperty
    private Usecase usecase;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "supply_channel_id")
    @JsonProperty
    private SupplyChannel supplyChannel;
}
