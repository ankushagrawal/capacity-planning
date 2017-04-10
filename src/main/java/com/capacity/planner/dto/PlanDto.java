package com.capacity.planner.dto;

import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

/**
 * Created by ankush.a on 05/04/17.
 */
@Data
@JsonSnakeCase
public class PlanDto {

    private Long quantity;

    private Long manHoursRequired;

    private UsecaseEnum usecase;
}
