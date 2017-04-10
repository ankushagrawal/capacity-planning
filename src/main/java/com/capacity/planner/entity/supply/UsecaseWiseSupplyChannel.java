package com.capacity.planner.entity.supply;

import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.entity.supply.SupplyChannel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Data
@Entity
@Table(name = "usecase_wise_supply_channel")
@TableGenerator(name = "idGen",table = "ID_GEN")
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

    @Column
    @JsonProperty
    private Double cost;
}
