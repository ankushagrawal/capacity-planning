package com.capacity.planner.dto;

import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.enums.SkillEnum;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.Set;

/**
 * Created by ankush.a on 06/04/17.
 */
@Data
@JsonSnakeCase
public class SupplyChannelResponseDto {

    private String supplyChannel;
//    private Set<SkillEnum> skillSets;
    private Double cost;
    private Long manHoursAvailable;

    private String usecaseIds;

    @Override
    public String toString() {
        return "SupplyChannelResponseDto{" +
                "supplyChannel='" + supplyChannel + '\'' +
                ", cost=" + cost +
                ", manHoursAvailable=" + manHoursAvailable +
                ", usecaseIds='" + usecaseIds + '\'' +
                '}';
    }
}
