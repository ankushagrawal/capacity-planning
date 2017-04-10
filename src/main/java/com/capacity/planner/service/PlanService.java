package com.capacity.planner.service;

import com.capacity.planner.dto.PlanDto;
import com.capacity.planner.dto.ResultDto;

import java.util.List;

/**
 * Created by ankush.a on 05/04/17.
 */
public interface PlanService {

    Long plan(List<PlanDto> planList) throws Exception;

    ResultDto getPlan(Long requestId) throws Exception;
}
