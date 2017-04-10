package com.capacity.planner.dto;

import com.capacity.planner.enums.UsecaseEnum;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;

/**
 * Created by ankush.a on 07/04/17.
 */
@Data
@JsonSnakeCase
public class DemandDto {

    private UsecaseEnum usecase;
    private Long manHoursRequired;

    @Override
    public String toString() {
        return "DemandDto{" +
                "usecase=" + usecase +
                ", manHoursRequired=" + manHoursRequired +
                '}';
    }
}
