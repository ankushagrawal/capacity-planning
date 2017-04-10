package com.capacity.planner.entity.metadata;

import com.capacity.planner.entity.supply.UsecaseWiseSupplyChannel;
import com.capacity.planner.enums.UsecaseEnum;
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
    private UsecaseEnum usecase;

    @OneToMany(mappedBy = "usecase",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UsecaseSkills> skills = new ArrayList<>();

    @OneToMany(mappedBy = "usecase",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UsecaseWiseSupplyChannel> supplyChannels = new ArrayList<>();

    @Override
    public String toString() {
        return "Usecase{" +
                "id=" + id +
                ", usecase=" + usecase +
                ", skills=" + skills +
                ", supplyChannels=" + supplyChannels +
                '}';
    }
}
