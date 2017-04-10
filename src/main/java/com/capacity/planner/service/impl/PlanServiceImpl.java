package com.capacity.planner.service.impl;

import com.capacity.planner.dao.*;
import com.capacity.planner.dto.*;
import com.capacity.planner.engine.impl.ResourceAllocationEngine;
import com.capacity.planner.entity.demand.Demand;
import com.capacity.planner.entity.metadata.Usecase;
import com.capacity.planner.entity.metadata.UsecaseSkills;
import com.capacity.planner.entity.outcome.PlanOutcome;
import com.capacity.planner.entity.supply.Supply;
import com.capacity.planner.entity.supply.SupplyChannel;
import com.capacity.planner.entity.supply.UsecaseWiseSupplyChannel;
import com.capacity.planner.enums.SkillEnum;
import com.capacity.planner.enums.UsecaseEnum;
import com.capacity.planner.service.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.stream;

/**
 * Created by ankush.a on 05/04/17.
 */
public class PlanServiceImpl implements PlanService{

    private DemandDao demandDao;
    private UsecaseDao usecaseDao;
    private ResourceAllocationEngine resourceAllocationEngine;
    private PlanOutcomeDao planOutcomeDao;

    public PlanServiceImpl(DemandDao demandDao, UsecaseDao usecaseDao,
                           SupplyChannelDao supplyChannelDao, SupplyDao supplyDao,PlanOutcomeDao planOutcomeDao) {
        this.demandDao = demandDao;
        this.usecaseDao = usecaseDao;
        this.supplyChannelDao = supplyChannelDao;
        this.planOutcomeDao = planOutcomeDao;
        this.supplyDao = supplyDao;
        resourceAllocationEngine = new ResourceAllocationEngine();
    }

    private SupplyChannelDao supplyChannelDao;
    private SupplyDao supplyDao;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(20);





    @Override
    public Long plan(List<PlanDto> planList) throws Exception{
        List<Demand> demandList = new ArrayList<>();
        for(PlanDto planDto : planList){
            Demand demand = new Demand();
            demand.setManHoursRequired(planDto.getManHoursRequired());
            demand.setQuantity(planDto.getQuantity());
            System.out.println("**************************** usecase = "+planDto.getUsecase());
            Usecase usecase = usecaseDao.getUsecase(planDto.getUsecase());
            if(usecase == null)
                throw  new Exception("No usecase found!");
            demand.setUsecase(usecase);
            demandList.add(demand);
        }
        Long requestId = demandDao.save(demandList);

        //Now, asynchronously trigger the supply channels for the request data.
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                getSupply(requestId);
            }
        });

            return requestId;
    }

    @Override
    public ResultDto getPlan(Long requestId) throws Exception {
        return planOutcomeDao.searchOutcome(requestId);
    }

    private void getSupply(Long requestId) {
        List<Demand> demandList = demandDao.searchDemand(requestId);
        //get skills required for each demands
        Map<String,Map<Usecase,Set<SkillEnum>>> supplySkillMap = new HashMap<>();
        for(Demand demand : demandList){
            List<UsecaseWiseSupplyChannel> supplyChannels = demand.getUsecase().getSupplyChannels();
            List<UsecaseSkills> usecaseSkillsList = demand.getUsecase().getSkills();
            Set<SkillEnum> skills = new HashSet<>();
            for(UsecaseSkills usecaseSkills : usecaseSkillsList) {
                skills.add(usecaseSkills.getSkill().getSkillName());
            }

            for(UsecaseWiseSupplyChannel supplyChannel : supplyChannels){
                if(supplySkillMap.containsKey(supplyChannel.getSupplyChannel().getSupplyChannel())){
                    Map<Usecase,Set<SkillEnum>> supplySkills = new HashMap<>();
                    supplySkills.putAll(supplySkillMap.get(supplyChannel.getSupplyChannel().getSupplyChannel()));
                    supplySkills.put(supplyChannel.getUsecase(),skills);
                    supplySkillMap.put(supplyChannel.getSupplyChannel().getSupplyChannel(), supplySkills);
                }
                else{
                    Map<Usecase,Set<SkillEnum>> supplySkills = new HashMap<>();
                    supplySkills.put(supplyChannel.getUsecase(),skills);
                    supplySkillMap.put(supplyChannel.getSupplyChannel().getSupplyChannel(),supplySkills);
                }
            }
        }

        //get supply for all skills
        askSupplyChannel(supplySkillMap, requestId);


    }

    private void askSupplyChannel(Map<String, Map<Usecase, Set<SkillEnum>>> supplySkillMap,Long requestId) {
        Iterator it = supplySkillMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Map<Usecase, Set<SkillEnum>>> pair = (Map.Entry)it.next();
            //for each supply channel, get supply data
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
            List<SupplyChannelResponseDto> supplyChannelResponseDtoList =
                    getSupplyData(pair.getKey(), pair.getValue());
            storeSupplyData(supplyChannelResponseDtoList,requestId);
//                }
//            });

        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                planDistributionOfJobs(requestId);
            }
        });
    }

    private void storeSupplyData(List<SupplyChannelResponseDto> supplyChannelResponseDtoList,Long requestId) {
        List<Supply> supplyList = new ArrayList<>();
        for(SupplyChannelResponseDto supplyChannelResponseDto : supplyChannelResponseDtoList){
            System.out.println("**************** supply channel response : " + supplyChannelResponseDto);
            Supply supply = new Supply();
            SupplyChannel supplyChannel =
                    supplyChannelDao.getSupplyChannel(supplyChannelResponseDto.getSupplyChannel());
            supply.setSupplyChannel(supplyChannel);
//            supply.setAmountPerHour(supplyChannelResponseDto.getCost());
            supply.setManHoursAvailable(supplyChannelResponseDto.getManHoursAvailable());
            supply.setRequestId(requestId);

            supply.setUsecaseListIds(supplyChannelResponseDto.getUsecaseIds());
            System.out.println("********************** saving supply : " + supply.getSupplyChannel().getSupplyChannel()
                    + " and usecase ids : " + supply.getUsecaseListIds());
            supplyList.add(supply);
        }
        supplyDao.save(supplyList);



    }

    private void planDistributionOfJobs(Long requestId) {
        //now map the supply data in the correct format for engine/planning

        //create demand matrix
        List<Demand> demandList = demandDao.searchDemand(requestId);
        int[] demandArray = new int[demandList.size()];
        System.out.println("request id : " + requestId);
        List<Supply> supplyList = supplyDao.searchSupply(requestId);
//        System.out.println("supplyList size  : "+supplyList.size());
        int[] supplyArray = new int[supplyList.size()];
        Map<Integer,String> demandIndex = new HashMap<>();
        Map<Integer,Supply> supplyIndex = new HashMap<>();
        int i = 0;
        for(Demand demand: demandList){
            demandArray[i] = demand.getManHoursRequired().intValue();
            demandIndex.put(i,demand.getUsecase().getId().toString());
            i++;
        }
        i = 0;
        System.out.println("demand index: "+demandIndex);
//        System.out.println("supply index: ");
//        for(Supply s : supplyIndex){
//            System.out.print(s.getUsecaseListIds()+" ");
//        }
        for(Supply supply: supplyList){
            supplyArray[i] = supply.getManHoursAvailable().intValue();
            supplyIndex.put(i,supply);
            i++;

        }
        //create cost matrix based on supply v/s demand
        int[][] costMatrix = new int[supplyList.size()][demandList.size()];
        for(int row = 0;row < supplyList.size();row++){
            Supply supplyRow = supplyIndex.get(row);
            for(int col = 0;col < demandList.size();col++){
                String[] usecaseRowIds = supplyRow.getUsecaseListIds().split("&");
                String demandUsecaseId = demandIndex.get(col);
                boolean edgeExist = false;
                for(String s: usecaseRowIds){
                    if(s.equalsIgnoreCase(demandUsecaseId)){
                        edgeExist = true;
                        break;
                    }
                }

                if(edgeExist){

                    List<UsecaseWiseSupplyChannel> usecaseWiseSupplyChannelList =
                            supplyRow.getSupplyChannel().getSupplyChannels();
                    for(UsecaseWiseSupplyChannel usecaseWiseSupplyChannel : usecaseWiseSupplyChannelList){
                        if(usecaseWiseSupplyChannel.getUsecase().getId().toString().equals(demandUsecaseId)) {
//                            System.out.println("edge between supply channel : "+usecaseWiseSupplyChannel.getSupplyChannel().getSupplyChannel()
//                                    +" and demand id exist : "+demandIndex.get(col)+" , supply usecase id : "+usecaseRowIds);
                            costMatrix[row][col] = usecaseWiseSupplyChannel.getCost().intValue();
                        }

                    }

                }
                else
                    costMatrix[row][col] = 10000;//hard coded to some high value
            }
        }

        System.out.println("demand array : " + Arrays.toString(demandArray).toString());
        System.out.println("supply array : " + Arrays.toString(supplyArray).toString());
        System.out.println("*************************** cost matrix: ");
        stream(costMatrix).forEach(a -> System.out.println(Arrays.toString(a)));
        System.out.println("*************************** done cost matrix: ");
        int[][] results = resourceAllocationEngine.distribute(requestId,demandArray,supplyArray,costMatrix);
        System.out.println("*************************** result matrix: ");
        stream(results).forEach(a -> System.out.println(Arrays.toString(a)));


        //store outcome
        storeOutcome(demandList,demandIndex,supplyIndex,requestId,costMatrix,results);



    }

    private void storeOutcome(List<Demand> demandList, Map<Integer, String> demandIndex,
                              Map<Integer, Supply> supplyIndex, Long requestId, int[][] costMatrix, int[][] results) {


        List<DemandDto> demandDtoList = new ArrayList<>();
        for(Demand demand : demandList){
            DemandDto demandDto = new DemandDto();
            demandDto.setUsecase(demand.getUsecase().getUsecase());
            demandDto.setManHoursRequired(demand.getManHoursRequired());
            demandDtoList.add(demandDto);
        }

        ResultDto resultDto = new ResultDto();
        resultDto.setRequestId(requestId);
        resultDto.setDemand(demandDtoList);
        List<SupplyDto> supplyDtos = new ArrayList<>();
        Iterator it = supplyIndex.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer,Supply> pair = (Map.Entry)it.next();
            SupplyDto supplyDto = new SupplyDto();
            List<AllotmentDto> allotmentDtoList = new ArrayList<>();
            supplyDto.setChannel(pair.getValue().getSupplyChannel().getSupplyChannel());
            supplyDto.setManHoursAvailable(pair.getValue().getManHoursAvailable());

            for(int col = 0; col < demandList.size(); col++) {

                if(results[pair.getKey()][col]!=0) {

                    AllotmentDto allotmentDto = new AllotmentDto();

                    String[] usecaseIds = pair.getValue().getUsecaseListIds().split("&");
                    List<UsecaseEnum> usecaseEnums = new ArrayList<>();
                    for (String s : usecaseIds) {
                        UsecaseEnum usecaseEnum = usecaseDao.getUsecase(Long.parseLong(s));
                        usecaseEnums.add(usecaseEnum);
                    }
                    supplyDto.setUsecasesAvailable(usecaseEnums);

                    allotmentDto.setUsecaseAllocated(usecaseDao.getUsecase(Long.parseLong(demandIndex.get(col))));
                    allotmentDto.setManHoursAllocated(Integer.toUnsignedLong(results[pair.getKey()][col]));
                    allotmentDto.setCost((double) costMatrix[pair.getKey()][col]);
                    allotmentDtoList.add(allotmentDto);

                }


            }
            supplyDto.setAllocations(allotmentDtoList);
            supplyDtos.add(supplyDto);
        }

        resultDto.setSupply(supplyDtos);
        System.out.println("result : " + resultDto);

        PlanOutcome planOutcome = new PlanOutcome();
        planOutcome.setRequestId(requestId);

        try {
            planOutcome.setOutcome(new ObjectMapper().writeValueAsString(resultDto));
        }catch (Exception e){
            e.printStackTrace();
        }

        planOutcomeDao.save(Arrays.asList(planOutcome));
    }


    private List<SupplyChannelResponseDto> getSupplyData(String supplyChannel,
                                                         Map<Usecase, Set<SkillEnum>> skillSetsRequired) {
        List<SupplyChannelResponseDto> supplyChannelResponseDtoList = new ArrayList<>();
        if("FQUICK".equalsIgnoreCase(supplyChannel)){
            Iterator it = skillSetsRequired.entrySet().iterator();
            int i = 1;
            while (it.hasNext()){
                Map.Entry<Usecase, Set<SkillEnum>> pair = (Map.Entry)it.next();
                SupplyChannelResponseDto supplyChannelResponseDto = new SupplyChannelResponseDto();
                supplyChannelResponseDto.setSupplyChannel(supplyChannel);
                supplyChannelResponseDto.setUsecaseIds(pair.getKey().getId().toString());
                supplyChannelResponseDto.setCost(50.0 * i);
//                supplyChannelResponseDto.setSkillSets(pair.getValue());
                supplyChannelResponseDto.setManHoursAvailable(100L * i);
                i++;
                supplyChannelResponseDtoList.add(supplyChannelResponseDto);
            }
        }
        else if("ADM".equalsIgnoreCase(supplyChannel)){
            Iterator it = skillSetsRequired.entrySet().iterator();
            int i = 1;
            while (it.hasNext()){
                Map.Entry<Usecase, Set<SkillEnum>> pair = (Map.Entry)it.next();
                SupplyChannelResponseDto supplyChannelResponseDto = new SupplyChannelResponseDto();
                supplyChannelResponseDto.setSupplyChannel(supplyChannel);
                supplyChannelResponseDto.setUsecaseIds(pair.getKey().getId().toString());
                supplyChannelResponseDto.setCost(40.0 * i);
//                supplyChannelResponseDto.setSkillSets(pair.getValue());
                supplyChannelResponseDto.setManHoursAvailable(80L * i);
                i++;
                supplyChannelResponseDtoList.add(supplyChannelResponseDto);
            }
        }

        if("FQUICK".equalsIgnoreCase(supplyChannel)) {
            Iterator it = skillSetsRequired.entrySet().iterator();
            int i = 1;
            String usecaseIds = null;
            while (it.hasNext()){
                Map.Entry<Usecase, Set<SkillEnum>> pair = (Map.Entry)it.next();
                System.out.println("********************************** " +
                        "usecase = " + pair.getKey().getUsecase().toString());
                if(i == 1)
                    usecaseIds = pair.getKey().getId().toString();
                else
                    usecaseIds = usecaseIds + "&" + pair.getKey().getId().toString();
                i++;
            }
            SupplyChannelResponseDto supplyChannelResponseDto = new SupplyChannelResponseDto();
            supplyChannelResponseDto.setSupplyChannel(supplyChannel);
            supplyChannelResponseDto.setUsecaseIds(usecaseIds);
            supplyChannelResponseDto.setCost(70.0);
//            supplyChannelResponseDto.setSkillSets(pair.getValue());
            supplyChannelResponseDto.setManHoursAvailable(30L);
            supplyChannelResponseDtoList.add(supplyChannelResponseDto);
        }
        System.out.println("*************************** response list : " + supplyChannelResponseDtoList);
        return supplyChannelResponseDtoList;
    }
}
