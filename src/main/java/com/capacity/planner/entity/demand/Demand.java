package com.capacity.planner.entity.demand;

import com.capacity.planner.entity.metadata.Usecase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name = "demand")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @Column
    @JsonProperty
    private Long quantity;

    @Column(name = "man_hours_required")
    @JsonProperty
    private Long manHoursRequired;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "usecase_id")
    @JsonProperty
    private Usecase usecase;

    @Column(name = "request_id")
    @JsonProperty
    private Long requestId;

    @Override
    public String toString() {
        return "Demand{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", manHoursRequired=" + manHoursRequired +
                ", usecase=" + usecase +
                ", requestId=" + requestId +
                '}';
    }
}
