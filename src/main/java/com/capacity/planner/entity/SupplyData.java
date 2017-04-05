package com.capacity.planner.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name = "supply_data")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class SupplyData {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @Column(name = "request_id")
    @JsonProperty
    private Long requestId;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "supply_channel_id")
    @JsonProperty
    private SupplyChannel supplyChannel;

    @Column(name = "man_hours_available")
    @JsonProperty
    private Long manHoursAvailable;


}
