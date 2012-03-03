package flashcache;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.LoaderHelper;
import com.gemstone.gemfire.cache.Region;

/** 
 * This class implements a CacheLoader used by the Quote program to
 * search for quotes in other caches and, failing that, to pull quotes
 * from the NASDAQ FlashQuote web site.
 *
 * <p>
 *
 * When a <code>CacheLoader</code> is associated with a {@link
 * Region}, GemFire will use it to load requested objects when there
 * is a cache-miss.
 *
 * @author GemStone Systems, Inc.
 *
 * @since 2.0
 */
@Component
public class QuoteLoader implements CacheLoader<String, Object>
{

/** this method is invoked by GemFire when there is a cache miss */
public Object load(LoaderHelper<String, Object> helper)
{
  String key = helper.getKey();  // the name of the cache entry

  // first check other caches but don't invoke their CacheLoaders
  System.out.println("(QuoteLoader netSearching for " + key + ")");
  try {
  Object hit = helper.netSearch(false);
    if (hit != null) {
      return hit;
    } 
  }
  catch (Exception e) {
    System.out.println(e.toString());
  }

  // now query quote.nasdaq.com
  System.out.println("(QuoteLoader querying nasdaq for " + key + ")");
  return loadSnapshot(key);
}

/** this method is invoked by the load() method when the requested quote
    isn't in the cache system and must be loaded from the NASDAQ web site */
String loadSnapshot(String symbolToLoad) {
  String page = "";
  String symbol = symbolToLoad;
  try {
    InputStream result;
    InputStreamReader reader;
    char[] buffer = new char[100000];
    int numchars;
    
    if (symbol == null)
      symbol = "JAVA";

    // the following code queries the quotes.nasdaq.com service and 
    // parses out the information needed to form the quote.  There is
    // retry logic in case nasdaq fails to respond

    boolean done = false;
    int maxTries = 4;
    for (int tries=0; !done && (tries < maxTries); tries++) {

      // note that nasdaq may charge for quote lookup services
      
      if (tries > 0) {
        page = "";  // reset the received portion of the web page
      }

      URL lookupURL = new URL(
//        "http://quotes.nasdaq.com/quote.dll?page=quick&mode=stock&symbol="
    	  "http://www.webservicex.net/stockquote.asmx/GetQuote?symbol="
        + symbol
        );
    
    
      String endTag = "</string>";
//      String endTag = "</HTML>";
      result = lookupURL.openStream();
      reader = new InputStreamReader(result);
      try {
        int innerloops = 0;
        do {
          innerloops++;
          for (int i=0; i<100; i++) {
            if (result.available() > 0) {
              break;
            }
            try { Thread.sleep(100); } catch (InterruptedException e) { break; }
          }
          if (result.available() > 0) {
            numchars = reader.read(buffer, 0, buffer.length);
            page = page + new String(buffer, 0, numchars);
          }
          done = page.indexOf(endTag, Math.max(page.length() - 100, 0)) > 0;
        } while (innerloops <= 100 && !done);
      }
      finally {
        result.close();
        reader.close();
      }
      
    } // for (tries)

    if (!done) {
      System.out.println("nasdaq did not respond in " + maxTries + " attempts.  Loader is returning null");
      return null;
    }

    String answer = symbol + ":";
//    int idx = page.indexOf("symbol="+symbol);
    int idx = page.indexOf(symbol);
    
    if (idx < 0) {
      return answer + "(invalid symbol)";
    } 

    String lastSaleElement = "&lt;Last&gt;";
    idx = page.indexOf(lastSaleElement) + lastSaleElement.length();
    int idx2 = page.indexOf("&lt;/Last&gt");
    answer = answer + " last sale=" + page.substring(idx, idx2);

    String netChange = "&lt;Change&gt;";
    idx = page.indexOf(netChange);
    idx2 = page.indexOf("&lt;/Change&gt;&lt;");
    netChange = page.substring(idx + netChange.length(), idx2);

    String color = "unknown";
    if (netChange.contains("+")) {
    	color = "green";
    } else if (netChange.contains("-")) {
    	color = "red";
    } else {
        netChange = "unch";
      }

    answer = answer + "  net change=" + netChange;
    
    String volumeElement = "&lt;Volume&gt"; 
    idx = page.indexOf(volumeElement);
    idx2 = page.indexOf("&lt;/Volume&gt;");
    answer = answer + "  volume=" + page.substring(idx + volumeElement.length(), idx2);
    
    return answer;
  }  catch (Exception e) {
    e.printStackTrace();
    //return page;
    return symbol+": not found";
  }
}

/** This method would clean up any database connections or other 
  * external resources.
  */
  public void close() {

  }

/** This is a test driver - this loads a quote without looking in
  * the GemFire cache.<p>
  * The class {@link Quote} uses GemFire to cache quotes.
  */
public static void main(String args[]) {
  if (args.length < 1) {
    System.out.println("java examples.flashcache.QuoteLoader [symbol]*");
    System.exit(1);
  }
  QuoteLoader instance = new QuoteLoader();
  for (int i=0; i<args.length; i++) {
    System.out.println(instance.loadSnapshot(args[i]));
  }
}

}
