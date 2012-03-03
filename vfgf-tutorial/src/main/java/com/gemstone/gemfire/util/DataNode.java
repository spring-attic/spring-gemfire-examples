package com.gemstone.gemfire.util;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.control.RebalanceOperation;
import com.gemstone.gemfire.cache.control.RebalanceResults;
import com.gemstone.gemfire.cache.control.ResourceManager;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;


/**
 * Cache Server
 * 
 */
public class DataNode {
    
    public static int batchSize = 5000;
    
    public static void main(String[] args) throws Exception {
        String resource = "spring-cache-server.xml";
        ClassPathXmlApplicationContext mainContext = new ClassPathXmlApplicationContext(new String[] {resource}, false);
        mainContext.setValidating(true);
        mainContext.refresh();
        userMenu(mainContext);
    }

    public static void printMenu() {
        System.out.println();
        System.out.println("1. Populate Data              (Use case 1)");
        System.out.println("2. Update domain data         (Use case 2)");
        System.out.println("3. Find Domain Data            (Use case 3)");
        System.out.println("4. Delete Domain Data           (Use case 4)");
        System.out.println("5. Inbound/Outbound Count     (Use case 5)");
        System.out.println("6. Test Failure               (Use case 6)");
        System.out.println("7. Inbound/Outbound Passenger (Use case 7)");
        System.out.println("8. Identify Missed Conn       (Use case 8)");
        System.out.println("9. Write- Behind/Through      (Use case 9)");
        System.out.println("10. Dynamic Scaling Up        (Use case 10)");
        System.out.println("11. Continuous Query          (Use case 11)");
        System.out.println("12. Missed Conn Listener      (Use case 12)");
        System.out.print("Your choice:");

    }

    public static void userMenu(ApplicationContext mainContext) throws Exception {
        while (true) {
            printMenu();
            Scanner s = new Scanner(System.in);
            int choice = s.nextInt();
            switch (choice) {
                case 1: {
                    useCase1_Main(mainContext);
                    break;
                }
                case 2: {
                    System.err.println("Not implemented.");
                    break;
                }
                case 3: {
                    System.out.println("\t1. Execute distributed function");
                    System.out.println("\t2. Execute system query");
                    int n = s.nextInt();
                    if (n == 1) {
                        useCase3_Function(mainContext);
                    } else {
                        useCase3_Query(mainContext);
                    }
                    break;
                }
                case 4: {
                    useCase4_DeletePassenger(mainContext);
                    break;
                }
                case 5: {
                    useCase5_FindInboundOutboundCount(mainContext);
                    break;
                }
                case 6: {
                    System.err.println("Not implemented.");
                    break;
                }
                case 7: {
                    useCase7_FindInboundOutboundPassengers(mainContext);
                    break;
                }
                case 8: {
                    useCase8_FindMissedConnections(mainContext);
                    break;
                }
                case 9: {
                    useCase9_WriteBehind(mainContext);
                    break;
                }
                case 10: {
                    useCase10_Rebalance(mainContext);
                    break;
                }
                case 11: {
                    System.out.println("Start a client and run use case 1 to see updates.");
                    System.out.println();
                    break;
                }
                case 12: {
                    useCase12(mainContext);
                    break;
                }
            }
        }
    }

    public static void useCase1_Main(ApplicationContext mainContext) throws Exception {
        Region<CacheMetaData, Boolean> meta = (Region) mainContext.getBean("cache-meta-data");
        meta.put(CacheMetaData.DISABLE_ALL_CACHE_LISTENERS, Boolean.TRUE);
        long start = System.currentTimeMillis();
        int itemsParsed = useCase1_ParseDomainData(mainContext);
        long stop1 = System.currentTimeMillis();
        long stop = System.currentTimeMillis();
        meta.remove(CacheMetaData.DISABLE_ALL_CACHE_LISTENERS);
        System.out.println("\nParse complete in: " + (stop - start) + " ms." + 
            "\n\tFligts=" + itemsParsed + " in "+(stop1-start) + " ms. (" + (itemsParsed / ((stop1-start)/1000)) + ") rec/s");
    }

    public static int useCase1_ParseDomainData(ApplicationContext mainContext) throws Exception {
        int cnt = 0;
          	/* Translate to domain data
        Region<FlightKey, FlightLeg> flights = (Region) mainContext.getBean("flt-region");
        FlatFileItemReader reader = (FlatFileItemReader) mainContext.getBean("flightItemReader");
        Map<FlightKey, FlightLeg> map = new HashMap<FlightKey, FlightLeg>();
        reader.open(new ExecutionContext());
        FlightLeg leg = (FlightLeg) reader.read();
        while (leg != null) {
            map.put(leg.getFlightKey(), leg);
            if (map.size() == batchSize) {
                flights.putAll(map);
                map.clear();
                cnt+=batchSize;
                System.out.print(".");
            }
            leg = (FlightLeg) reader.read();
        }
        cnt+=map.size();
        flights.putAll(map);
        reader.close();
*/        
        return cnt;
    }

    public  static void useCase10_Rebalance(ApplicationContext mainContext) throws InterruptedException {
        Cache cache = (Cache) mainContext.getBean("cache");
        ResourceManager manager = cache.getResourceManager();
        RebalanceOperation op = manager.createRebalanceFactory().start();
        // Wait until the rebalance is complete
        RebalanceResults results = op.getResults();
        System.out.println("Rebalance Took " + results.getTotalTime() + " milliseconds\n");
        System.out.println("Rebalance Transfered " + results.getTotalBucketTransferBytes() + "bytes\n");
    }

    public  static void useCase3_Function(ApplicationContext mainContext) {
    	/* Find domain data
        FindPassengerBean findPassengerBean = (FindPassengerBean) mainContext.getBean("findPassengerBean");
        findPassengerBean.findPassengersFunction();
        */
    }

    public  static void useCase3_Query(ApplicationContext mainContext) {
    	/* Query Domain Data
        FindPassengerBean findPassengerBean = (FindPassengerBean) mainContext.getBean("findPassengerBean");
        findPassengerBean.findPassengersQuery();
        */
    }

    public  static void useCase4_DeletePassenger(ApplicationContext mainContext) {
        System.out.print("Enter PNR_LOC to delete:");
        String pnr = new Scanner(System.in).next();
        Region segmentRegion = (Region) mainContext.getBean("segment-region");
        Execution execution = FunctionService.onRegion(segmentRegion).withArgs(pnr);
        ResultCollector rc = execution.execute(((Function) mainContext.getBean("deletePassengerFunction")).getId());
        ArrayList<Integer> counts = (ArrayList<Integer>) rc.getResult();
        int count = 0;
        for (Integer cnt : counts) {
            count += cnt.intValue();
        }
        System.err.println("Deleted " + count + " passenger segments");

    }

    public  static void useCase5_FindInboundOutboundCount(ApplicationContext mainContext) {
    	/*
        int flightDate = 20110926;
        String depStation = "CHO";
        String arrStation = "ORD";
        int fltNumber = 4299;
        FlightKey key = new FlightKey();
        key.setFltDate(flightDate);
        key.setDepStation(depStation);
        key.setArrStation(arrStation);
        key.setFltNumber(fltNumber);
        Region segmentRegion = (Region) mainContext.getBean("segment-region");
        Function function = (Function) mainContext.getBean("inboundOutboundCountFunction");
        Execution execution = FunctionService.onRegion(segmentRegion).withArgs(key);
        ResultCollector rc = execution.execute(function.getId());
        List<InboundOutboundCount> counts = (List<InboundOutboundCount>) rc.getResult();
        int inbound = 0;
        int outbound = 0;
        for (InboundOutboundCount c : counts) {
            inbound += c.getInbound();
            outbound += c.getOutbound();
        }
        System.out.println("Flight:\n\t" + key + "\n\tInbound Passenger:" + inbound + "\n\tOutbound Passengers:" + outbound);
        */
    }

    public  static void useCase7_FindInboundOutboundPassengers(ApplicationContext mainContext) {
    	/*
        int flightDate = 20110926;
        String depStation = "CHO";
        String arrStation = "ORD";
        int fltNumber = 4299;
        FlightKey key = new FlightKey();
        key.setFltDate(flightDate);
        key.setDepStation(depStation);
        key.setArrStation(arrStation);
        key.setFltNumber(fltNumber);
        Region segmentRegion = (Region) mainContext.getBean("segment-region");
        Function function = (Function) mainContext.getBean("inboundOutboundPassengerFunction");
        Execution execution = FunctionService.onRegion(segmentRegion).withArgs(key);
        ResultCollector rc = execution.execute(function.getId());
        List<InboundOutboundPassenger> passengers = (List<InboundOutboundPassenger>) rc.getResult();
        System.out.println("Inbound Passengers:");
        for (InboundOutboundPassenger c : passengers) {
            for (Entry<Pnr, PnrSegment> pnr : c.getInbound().entrySet()) {
                System.out.println("\t"+pnr.getValue());
            }
        }
        System.out.println("Outbound Passengers:");
        for (InboundOutboundPassenger c : passengers) {
            for (Entry<Pnr, PnrSegment> pnr : c.getOutbound().entrySet()) {
                System.out.println("\t"+pnr.getValue());
            }
        }
        */
    }

    
    public  static void useCase8_FindMissedConnections(ApplicationContext mainContext) {
    	/*
        //this is our flight that will be late
        int flightDate = 20110926;
        String depStation = "CHO";
        String arrStation = "ORD";
        int fltNumber = 4299;
        FlightKey key = new FlightKey();
        key.setFltDate(flightDate);
        key.setDepStation(depStation);
        key.setArrStation(arrStation);
        key.setFltNumber(fltNumber);
        
        //retrieve the flight leg and set a new arrival time
        Region<FlightKey, FlightLeg> flightRegion = (Region<FlightKey, FlightLeg>) mainContext.getBean("flt-region");
        FlightLeg leg = flightRegion.get(key);
        leg.setLatestArrDateTime(201109262101l); //  9.01pm Oct 26, 2011
        long start = System.currentTimeMillis();
        //execute a distributed function
        Region segmentRegion = (Region) mainContext.getBean("segment-region");
        Function function = (Function) mainContext.getBean("missedPassengerConnectionsFunction");
        Execution execution = FunctionService.onRegion(segmentRegion).withArgs(leg);
        
        
        ResultCollector rc = execution.execute(function.getId());
        
        List<HashSet<SegmentKey>> missedpnrs = (List<HashSet<SegmentKey>>) rc.getResult();
        long stop = System.currentTimeMillis();
        for (HashSet<SegmentKey> segments : missedpnrs) {
            for (SegmentKey seg : segments) {
                System.out.println("Missed Flight:" + seg);
            }
        }
        System.out.println("Missed Connections Execution Took:"+(stop-start)+" ms.");
        */
    }
    
    public  static void useCase9_WriteBehind(ApplicationContext mainContext) {
        int cnt = 0;
        
        Region<String, Domain> pnrs = (Region) mainContext.getBean("pnr-region");
        Region<String, Domain> wb = (Region) mainContext.getBean("write-behind-region");
        
        for (Entry<String,Domain> entry : pnrs.entrySet()) {
            wb.put(entry.getKey(), entry.getValue());
            
        }
    }
    
    public  static void useCase12(ApplicationContext mainContext) {
    	/* Domain specific
        int flightDate = 20110926;
        String depStation = "CHO";
        String arrStation = "ORD";
        int fltNumber = 4299;
        FlightKey key = new FlightKey();
        key.setFltDate(flightDate);
        key.setDepStation(depStation);
        key.setArrStation(arrStation);
        key.setFltNumber(fltNumber);
        
        //retrieve the flight leg and set a new arrival time
        Region<FlightKey, FlightLeg> flightRegion = (Region<FlightKey, FlightLeg>) mainContext.getBean("flt-region");
        
        FlightLeg leg = flightRegion.get(key);
        
        
        leg.setLatestArrDateTime(201109262101l); //  9.01pm Oct 26, 2011
        
        flightRegion.put(leg.getFlightKey(), leg);
        */
    }
    

    public  static void useCaseX_getSegment(ApplicationContext mainContext) throws InterruptedException {
    	/*  Domain Specific
        // "10.97.84.8-2011.10.18-15:23:47","BQQIRC",2,2867,19-OCT-11
        // 12.00.00.000000000 AM,"DFW","XNA","1630",19-OCT-11 12.00.00.000000000
        // AM,"1735","Q","Y",1,"HK",1,"I","I","AA","","","",""
        SegmentKey key = new SegmentKey();
        key.getFlightKey().setArrStation("XNA");
        key.getFlightKey().setDepStation("DFW");
        key.getFlightKey().setFltDate(20111019);
        key.getFlightKey().setFltNumber(2867);
        key.setPnrLoc("BQQIRC");
        Region<SegmentKey, PnrSegment> segments = (Region) mainContext.getBean("segment-region");
        PnrSegment segment = segments.get(key);
        System.err.println("Segment:" + segment);
        */

    }
}
