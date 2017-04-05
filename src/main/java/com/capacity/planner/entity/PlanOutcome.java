package com.capacity.planner.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name = "plan_outcome")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class PlanOutcome {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    private Long id;

    @JsonProperty
    @Column(name = "request_id")
    private Long requestId;

    @JsonProperty
    private String outcome;

}
