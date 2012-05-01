package quickstart;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import com.gemstone.gemfire.cache.Declarable;

/**
 * A stock portfolio that consists of multiple {@link Position}
 * objects that represent shares of stock (a "security").  Instances
 * of <code>Portfolio</code> can be stored in a GemFire
 * <code>Region</code> and their contents can be queried using the
 * GemFire query service.
 * <P>
 * This class is <code>Serializable</code> because we want it to be
 * distributed to multiple members of a distributed system.  Because
 * this class is <code>Declarable</code>, we can describe instances of
 * it in a GemFire <code>cache.xml</code> file.
 *
 * @author GemStone Systems, Inc.
 * @since 4.1.1
 */
public class Portfolio implements Declarable, Serializable {
  private static final long serialVersionUID = 9097335119586059309L;
  
  private int id;  /* id is used as the entry key and is stored in the entry */
  private String type;
  private Map<String,Position> positions = new LinkedHashMap<String,Position>();
  private String status;
  
  public void init(Properties props) {
    this.id = Integer.parseInt(props.getProperty("id"));
    this.type = props.getProperty("type", "type1");
    this.status = props.getProperty("status", "active");
    
    // get the positions. These are stored in the properties object
    // as Positions, not String, so use Hashtable protocol to get at them.
    // the keys are named "positionN", where N is an integer.
    for (Map.Entry<Object, Object> entry: props.entrySet()) {
      String key = (String)entry.getKey();
      if (key.startsWith("position")) {
        Position pos = (Position)entry.getValue();
        this.positions.put(pos.getSecId(), pos);
      }
    }
  }
  
  public String getStatus(){
    return status;
  }
  
  public int getId(){
    return this.id;
  }
  
  public Map<String,Position> getPositions(){
    return this.positions;
  }
  
  public String getType() {
    return this.type;
  }
  
  public boolean isActive(){
    return status.equals("active");
  }
  
  public String toString(){
    StringBuffer out = new StringBuffer();
    out.append("\n\tPortfolio [id=" + this.id + " status=" + this.status);
    out.append(" type=" + this.type);
    boolean firstTime = true;
    for (Map.Entry<String, Position> entry: positions.entrySet()) {
      if (!firstTime) {
        out.append(", ");
      }
      out.append("\n\t\t");
      out.append(entry.getKey() + ":" + entry.getValue());
      firstTime = false;
    }
    out.append("]");
    return out.toString();
  }
}

