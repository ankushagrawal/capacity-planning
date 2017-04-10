package com.capacity.planner.entity.supply;

import com.capacity.planner.entity.metadata.Usecase;
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
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @Column(name = "request_id")
    @JsonProperty
    private Long requestId;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "supply_channel_id")
    private SupplyChannel supplyChannel;

    @Column(name = "man_hours_available")
    @JsonProperty
    private Long manHoursAvailable;

//    @Column(name = "amount_per_hour")
//    @JsonProperty
//    private Double amountPerHour;


    @JsonProperty
    @Column(name = "usecase_list_ids")
    private String usecaseListIds;


    @Override
    public String toString() {
        return "Supply{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", supplyChannel=" + supplyChannel.getSupplyChannel() +
                ", manHoursAvailable=" + manHoursAvailable +
                ", usecaseListIds='" + usecaseListIds + '\'' +
                '}';
    }
}
