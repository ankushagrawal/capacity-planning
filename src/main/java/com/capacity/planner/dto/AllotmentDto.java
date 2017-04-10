package com.capacity.planner.dto;

import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

/**
 * Created by ankush.a on 10/04/17.
 */
@Data
@JsonSnakeCase
public class AllotmentDto {

    private UsecaseEnum usecaseAllocated;
    private Long manHoursAllocated;
    private Double cost;
}
