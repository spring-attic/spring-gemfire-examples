package quickstart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

/**
 * 
 * MyArrayListResultCollector gathers result from all the function execution
 * nodes.<br>
 * Using a custom ResultCollector a user can sort/aggrgate the result. This
 * implementation stores the result in a List. The size of the list will be same
 * as the no of nodes on which a function got executed
 * 
 * @author Gemstone Systems Inc
 * @since 6.0
 * 
 */
public class MyArrayListResultCollector implements
    ResultCollector<Serializable,Serializable> {

  final ArrayList<Serializable> result = new ArrayList<Serializable>();

  /**
   * Adds a single function execution result from a remote node to the
   * ResultCollector
   * 
   * @param resultOfSingleExecution
   * @param memberID
   */
  public void addResult(DistributedMember memberID,
      Serializable resultOfSingleExecution) {
    this.result.add(resultOfSingleExecution);
  }

  /**
   * Waits if necessary for the computation to complete, and then retrieves its
   * result.<br>
   * If {@link Function#hasResult()} is false, upon calling
   * {@link ResultCollector#getResult()} throws {@link FunctionException}.
   * 
   * @return the Serializable computed result
   * @throws FunctionException
   *           if something goes wrong while retrieving the result
   */
  public Serializable getResult() throws FunctionException {
    return this.result;
  }

  /**
   * Waits if necessary for at most the given time for the computation to
   * complete, and then retrieves its result, if available. <br>
   * If {@link Function#hasResult()} is false, upon calling
   * {@link ResultCollector#getResult()} throws {@link FunctionException}.
   * 
   * @param timeout
   *          the maximum time to wait
   * @param unit
   *          the time unit of the timeout argument
   * @return Serializable computed result
   * @throws FunctionException
   *           if something goes wrong while retrieving the result
   */
  public Serializable getResult(long timeout, TimeUnit unit)
      throws FunctionException, InterruptedException {
    return this.result;
  }

  /**
   * GemFire will invoke this method before re-executing function (in case of
   * Function Execution HA) This is to clear the previous execution results from
   * the result collector
   * 
   * @since 6.3
   */
  public void clearResults() {
    result.clear();
  }
  
  /**
   * Call back provided to caller, which is called after function execution is
   * complete and caller can retrieve results using
   * {@link ResultCollector#getResult()}
   * 
   */
  public void endResults() {}

}
