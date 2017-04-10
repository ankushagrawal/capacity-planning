package com.capacity.planner.entity.supply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "supplyChannel",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<UsecaseWiseSupplyChannel> supplyChannels = new ArrayList<>();
}
