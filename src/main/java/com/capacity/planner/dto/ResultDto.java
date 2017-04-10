package com.capacity.planner.dto;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

import java.util.List;

/**
 * Created by ankush.a on 07/04/17.
 */
@Data
@JsonSnakeCase
public class ResultDto {

    private Long requestId;
    private List<DemandDto> demand;
    private List<SupplyDto> supply;


    @Override
    public String toString() {
        return "ResultDto{" +
                "requestId=" + requestId +
                ", demand=" + demand +
                ", supply=" + supply +
                '}';
    }
}
