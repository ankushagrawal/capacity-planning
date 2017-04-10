package com.capacity.planner.dto;

import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

/**
 * Created by ankush.a on 07/04/17.
 */
@Data
@JsonSnakeCase
public class SupplyDto {

    private String channel;
    private List<UsecaseEnum> usecasesAvailable;
    private Long manHoursAvailable;
    private List<AllotmentDto> allocations;

    @Override
    public String toString() {
        return "SupplyDto{" +
                "channel='" + channel + '\'' +
                ", usecasesAvailable=" + usecasesAvailable +
                ", manHoursAvailable=" + manHoursAvailable +
                ", allocations=" + allocations +
                '}';
    }
//    private UsecaseEnum usecaseAllocated;
//    private Long manHoursAllocated;
//    private Double cost;


}
