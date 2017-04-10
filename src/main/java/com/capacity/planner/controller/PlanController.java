package com.capacity.planner.controller;

import com.capacity.planner.dto.PlanDto;
import com.capacity.planner.dto.ResultDto;
import com.capacity.planner.service.PlanService;
import com.capacity.planner.service.impl.PlanServiceImpl;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ankush.a on 05/04/17.
 */
@Path("/plan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlanController {

    private PlanServiceImpl planService;

    public PlanController(PlanServiceImpl planService) {
        this.planService = planService;
    }

    @POST
    @Timed
    @Path("/demand/")
    public Response plan(List<PlanDto> planList) throws Exception{
        Long requestid = planService.plan(planList);
        Map<String,Object> response = new HashMap<>();
        response.put("success_code",Response.Status.ACCEPTED);
        response.put("success_message","Your request is successfully placed with request id : "+requestid);
        response.put("additional_message","Please check with the request ID for further details.");
        response.put("helpful_uri","http://localhost:8080/plan?request_id="+requestid);
        return Response.status(Response.Status.ACCEPTED).entity(response).build();
    }

    @GET
    @Timed
    @Path("/")
    public Response plan(@QueryParam("request_id") Long requestId) throws Exception{
        ResultDto resultDto  = planService.getPlan(requestId);
        return Response.status(Response.Status.ACCEPTED).entity(resultDto).build();
    }
}
