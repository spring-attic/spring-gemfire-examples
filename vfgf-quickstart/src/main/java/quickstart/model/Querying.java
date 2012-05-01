package quickstart.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.SelectResults;

/**
 * You can query on the data in your cached regions. Please refer to the 
 * quickstart guide for instructions on how to run this example. 
 * 
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
@Component
public class Querying {
	@Resource(name="gemfire-cache")
	Cache cache;
	
	@Resource(name = "exampleRegion")
    Region<?,?> exampleRegion;
	
  public void run() throws Exception {
    System.out.println("\nThis example demonstrates querying on a set of data in a GemFire ");
    System.out.println("region. The data represents Portfolios containing Positions. ");

    System.out.println("\nConnecting to the distributed system and creating the cache.");
    System.out.println("Example region, " + exampleRegion.getFullPath() + ", created in cache. ");

    // Get the query service for the cache
    QueryService queryService = cache.getQueryService();
    Query query = null;
    Object result = null;
    
    // Query everything to show what's in the region
    query = queryService.newQuery("SELECT DISTINCT * FROM /exampleRegion");
    System.out.println("\nExecuting query:\n\t" + query.getQueryString()); 
    result = query.execute();
    System.out.println("Query result:\n\t" + formatQueryResult(result));
    
    System.out.println("\nPress Enter to continue to next query...");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
    
    // Execute a simple query on the exampleRegion 
    query = queryService.newQuery("/exampleRegion.isDestroyed");
    System.out.println("\nExecuting query:\n\t" + query.getQueryString()); 
    result = query.execute();
    System.out.println("Query result:\n\t" + formatQueryResult(result));
    
    System.out.println("\nPress Enter to continue to next query...");
    // bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
    
    // Execute a query with SELECT, FROM and WHERE clauses
    query = queryService.newQuery(
      "SELECT DISTINCT * FROM /exampleRegion WHERE status = 'active' ORDER by 'type'");
    System.out.println("Executing query:\n\t" + query.getQueryString()); 
    result = query.execute();
    System.out.println("Query result:\n\t" + formatQueryResult(result));
    
    System.out.println("Press Enter to continue to next query...");
    // bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
    
    // Execute a query with a compound WHERE clause 
    query = queryService.newQuery(
      "SELECT DISTINCT * FROM /exampleRegion " +
      "WHERE status = 'active' AND \"type\" = 'type3'");
    System.out.println("Executing query:\n\t" + query.getQueryString()); 
    result = query.execute();
    System.out.println("Query result:\n\t" + formatQueryResult(result));

    System.out.println("Press Enter to continue to next query...");
    // bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedReader.readLine();
    
    // Execute a more complex query
    query = queryService.newQuery(
      "IMPORT quickstart.model.Position; " + 
      "SELECT DISTINCT posnVal " +
      "FROM /exampleRegion, positions.values posnVal TYPE Position " +
      "WHERE status = 'active' AND posnVal.mktValue >= 25.00");
    System.out.println("Executing query:\n\t" + query.getQueryString()); 
    result = query.execute();
    System.out.println("Query result:\n\t" + formatQueryResult(result));

    // Close the cache and disconnect from GemFire distributed system
    System.out.println("Closing the cache and disconnecting.");
    cache.close();
  }
  
  public static String formatQueryResult(Object result) {
    if (result == null) {
    	return "null";
    }
    else if (result == QueryService.UNDEFINED) {
      return "UNDEFINED";
    }
    if (result instanceof SelectResults) {
      Collection<?> collection = ((SelectResults<?>)result).asList();
      StringBuffer sb = new StringBuffer();
      for (Object e: collection) {
        sb.append(e + "\n\t");
      }
      return sb.toString();
    }
    else {
    	return result.toString();
    }
  }
  
}

