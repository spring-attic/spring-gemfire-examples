package quickstart.model;

import java.io.Serializable;
import java.util.Properties;
import org.springframework.stereotype.Component;
import com.gemstone.gemfire.cache.Declarable;

/**
 * Represents a number of shares of a stock ("security") held in a
 * {@link Portfolio}.
 * <P>
 * This class is <code>Serializable</code> because we want it to be distributed
 * to multiple members of a distributed system. Because this class is
 * <code>Declarable</code>, we can describe instances of it in a GemFire
 * <code>cache.xml</code> file.
 * 
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
@Component
public class Position implements Serializable {
	private static final long serialVersionUID = -8229531542107983344L;

  
  private String secId;
  private double qty;
  private double mktValue;
  
  public String getSecId(){
    return this.secId;
  }
  
  public double getQty(){
    return this.qty;
  }
    
  public double getMktValue() {
    return this.mktValue;
  }

	public void setSecId(String secId) {
		this.secId = secId;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public void setMktValue(double mktValue) {
		this.mktValue = mktValue;
	}


  public String toString(){
    return "Position [secId="+secId+" qty="+this.qty+" mktValue="+mktValue+"]";
  }
}
