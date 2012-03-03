package systemExplorer;

import com.gemstone.gemfire.admin.AdminDistributedSystem;
import com.gemstone.gemfire.admin.AdminDistributedSystemFactory;
import com.gemstone.gemfire.admin.AdminException;
import com.gemstone.gemfire.admin.CacheVm;
import com.gemstone.gemfire.admin.ConfigurationParameter;
import com.gemstone.gemfire.admin.DistributedSystemConfig;
import com.gemstone.gemfire.admin.DistributionLocator;
import com.gemstone.gemfire.admin.DistributionLocatorConfig;
import com.gemstone.gemfire.admin.Statistic;
import com.gemstone.gemfire.admin.StatisticResource;
import com.gemstone.gemfire.admin.SystemMember;
import com.gemstone.gemfire.admin.SystemMemberCache;
import com.gemstone.gemfire.admin.SystemMemberRegion;
import com.gemstone.gemfire.admin.SystemMemberType;
import com.gemstone.gemfire.cache.AttributesFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * This class is a command-line application that allows the user to
 * exercise GemFire's {@link com.gemstone.gemfire.admin admin API}.
 *
 * @author GemStone Systems, Inc.
 * @since 3.5
 */
public class SystemExplorer {

  /** History of entities leading up to the current entity */
  private Stack<Entity> history = new Stack<Entity>();
  
  /** The currently selected administrative entity */
  private Entity entity;
  
  /** The administrative entity for the distributed system */
  private AdminDistributedSystem system;

  /** The latest selected system member cache */
  private SystemMemberCache cache;
  
  /**
   * Prints information on how this program should be used.
   */
  void showHelp() {
    PrintStream out = System.out;

    out.println();
    out.println("An admin distributed system is created with properties loaded from your gemfire.properties file.");
    out.println("You can specify alternative property files using -DgemfirePropertyFile=path.");
    out.println();
    out.println("Use this this program by selecting a context (such as a ");
    out.println("system member, or the system as a whole) and, within that context, performing operations. ");
    out.println();
    out.println("General:");
    out.println();
    out.println("system - select the distributed system.");
    out.println("help or ? - list command descriptions.");
    out.println("exit or quit: disconnect from the system and exit.");
    out.println();
    out.println("Distributed system commands:");
    out.println();
    out.println("   ls - lists all members and details of the distributed system");
    out.println("   define locator <host> <port> <workingdir> <productdir> [<remotecommand>]");
    out.println("     Administer the locator defined by the specified values.");
    out.println("     - host - the host machine or ip address for the locator.");
    out.println("     - port - the port that the locator listens on.");
    out.println("     - workingdir - the working directory for the locator and its log.");
    out.println("     - productdir - the gemfire product that the locator uses.");
    out.println("     - remotecommand - default is: rsh -n {HOST} {CMD}");
    out.println("   locator <name> - select the specified distribution locator.");
    out.println("   cacheserver <name> - select the specified gemfire cacheserver.");
    out.println("   member <name> - select the specified system member.");
    out.println("   start - start all configured locators and cacheservers.");
    out.println("   stop - stop all configured locators and cacheservers.");
    out.println();
    out.println("Distribution locator commands:");
    out.println();
    out.println("   ls - lists details of the distribution locator.");
    out.println("   start - start the distribution locator.");
    out.println("   stop - stop the distribution locator.");
    out.println();
    out.println("System member commands:");
    out.println("   start - start the selected cacheserver.");
    out.println("   stop - stop the selected cacheserver.");
    out.println("   ls - lists configuration, statistic resources and details of the member");
    out.println("   stat <name> - select the specified statistic resource.");
    out.println("   cache - select the member's cache.");
    out.println();
    out.println("Cache and region commands:");
    out.println();
    out.println("   createRoot <name> - create a root region in the current cache (default configuration).");
    out.println("   createSub <name> - create a subregion of the current region.");
    out.println("   region <name> - select the specified region or subregion.");
    out.println("   region .. - select the parent region of the current subregion.");
    out.println("   cache - select the cache that owns this region.");
    out.println("   member - select the member that owns this cache or region.");
    out.println();
    out.println("Statistic resource commands:");
    out.println();
    out.println("   ls - lists all the statistics values.");
    out.println("   member - select the member that owns this statistic resource.");
    out.println();
  }

  /**
   * Initializes the <code>DistributedSystem</code> for this example program.
   */
  void initialize(String propsFileName) throws Exception {
    if (propsFileName != null) {
      System.setProperty("gemfirePropertyFile", propsFileName);
    }

    AdminDistributedSystemFactory.setEnableAdministrationOnly(true);
    DistributedSystemConfig config = 
        AdminDistributedSystemFactory.defineDistributedSystem();
    config.setSystemName("SystemExplorer");
    this.system = AdminDistributedSystemFactory.getDistributedSystem(config);
    this.system.connect();
    // Wait for the connection to be made
    long timeout = 30 * 1000;
    try {
      if (!this.system.waitToBeConnected(timeout)) {
        String s = "Could not connect after " + timeout + "ms";
        throw new Exception(s);
      }
    }
    catch (InterruptedException ex) {
      String s = "Interrupted while waiting to be connected";
      throw new Exception(s, ex);
    }
    
    pushEntity(Context.DISTRIBUTED_SYSTEM, this.system);
  }
  
  /**
   * Parses the command line and runs the <code>SystemExplorer</code>
   * example.
   */
  public static void main(String[] args) throws Exception {
    if (args.length > 1) {
      System.err.println("Usage: java systemExplorer.SystemExplorer [<gemfire.properties>]");
      System.exit(1);
    }
    
    String propsFileName = null;
    if (args.length > 0) {
      propsFileName = args[0];
      File propsFile = new File(propsFileName);
      if (!propsFile.exists()) {
        System.err.println("Supplied GemFire properties file <gemfire.properties> does not exist");
        System.exit(1);
      }
    }
    
    SystemExplorer explorer = new SystemExplorer();
    explorer.initialize(propsFileName);
    explorer.go();
    System.exit(0);
  }

  /**
   * Prompts the user for input and executes the command accordingly.
   */
  void go() throws IOException {
    BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
    String command;

    System.out.println("Enter 'help' or '?' for help at the command prompt.");
    System.out.println("");

    while (true) {
      try {
        
        showPrompt();
        String line = bin.readLine();
        if (line == null) {
          // User typed EOF
          break;
        }
        command = line.trim();

        if (command.startsWith("exit") || command.startsWith("quit")) {
          this.system.disconnect();
          System.exit(0);
        }
        else if (command.startsWith("ls") || command.startsWith("list")) {
          list(command);
        }
        else if (command.startsWith("system")) {
          system(command);
        }
        else if (command.startsWith("member")) {
          member(command);
        }
        else if (command.startsWith("cacheserver")) {
          cacheserver(command);
        }
        else if (command.startsWith("locator")) {
          locator(command);
        }
        else if (command.startsWith("define locator")) {
          defineLocator(command);
        }
        else if (command.startsWith("cache") && !(command.startsWith("cacheserver"))) {
          cache(command);
        }
        else if (command.startsWith("region")) {
          region(command);
        }
        else if (command.startsWith("createRoot")) {
          createRootRegion(command);
        }
        else if (command.startsWith("createSub")) {
          createSubRegion(command);
        }
        else if (command.startsWith("start")) {
          start(command);
        }
        else if (command.startsWith("stop")) {
          stop(command);
        }
        else if (command.startsWith("stat")) {
          stat(command);
        }
        else if (command.startsWith("help") || command.startsWith("?")){
          showHelp();
        }
        else if (command.length() != 0) {
          System.out.println("Unrecognized command. Enter 'help' or '?' to get a list of commands.");
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // ************ Command implementation methods ****************************

  /**
   * Shows the user prompt from the currently selected entity.
   */
  void showPrompt() {
    System.out.print(this.entity.getContext() + ":" + 
                     this.entity.getObject() + "> ");
    System.out.flush();
  }
  
  /**
   * Lists the details for the currently selected admin entity.
   */
  void list(String command) throws AdminException {
    if (this.entity.getContext().isDistributedSystem()) {
      listDistributedSystem();
    }
    else if (this.entity.getContext().isDistributionLocator()) {
      listDistributionLocator();
    }
    else if (this.entity.getContext().isCacheServer()) {
      listCacheServer();
    }
    else if (this.entity.getContext().isSystemMember()) {
      listSystemMember();
    }
    else if (this.entity.getContext().isStatisticResource()) {
      listStatisticResource();
    }
    else if (this.entity.getContext().isSystemMemberCache()) {
      listSystemMemberCache();
    }
    else if (this.entity.getContext().isSystemMemberRegion()) {
      listSystemMemberRegion();
    }
  }
  
  /**
   * Defines a <code>DistributionLocator</code> so this example can 
   * manipulate it.
   */
  void defineLocator(String command) throws AdminException {
    String name = parseName(command);
    if ("locator".equals(name)) {
      StringTokenizer st = new StringTokenizer(command, " ");
      
      String host = null;
      int port = 0;
      String workingDirectory = null;
      String productDirectory = null;
      String remoteCommand =
        DistributedSystemConfig.DEFAULT_REMOTE_COMMAND;
      
      for (int i = 0; st.hasMoreTokens(); i++) {
        String next = st.nextToken();
        if (i == 2) {
          host = next;
        } else if (i == 3) {
          try {
            port = Integer.parseInt(next);
          } catch(NumberFormatException e) {
            System.err.println("You need to give a valid port for this command");
            return;
          }
        } else if (i == 4) {
          workingDirectory = next;
        } else if (i == 5) {
          productDirectory = next;
        } else if (i == 6) {
          remoteCommand = next;
        }
      }
      
      try {
        DistributionLocator locator =
          this.system.addDistributionLocator();
        DistributionLocatorConfig config = locator.getConfig();
        config.setHost(host);
        config.setPort(port);
        config.setWorkingDirectory(workingDirectory);
        config.setProductDirectory(productDirectory);
        config.setRemoteCommand(remoteCommand);
        config.validate();

      } catch (Exception e) {
        System.err.println("Failed to define locator: " + e.getMessage());
      }
    }
  }
  
  /**
   * Starts the currently selected administrative entity.
   */
  void start(String command) throws AdminException {
    if (this.entity.getContext().isDistributedSystem()) {
      AdminDistributedSystem sys =
        (AdminDistributedSystem) this.entity.getObject();
      sys.start();
    }
    else if (this.entity.getContext().isCacheServer()) {
      CacheVm csvr = (CacheVm) this.entity.getObject();
      csvr.start();
    }
    else if (this.entity.getContext().isDistributionLocator()) {
      DistributionLocator loc = (DistributionLocator) this.entity.getObject();
      loc.start();
    }
  }

  /**
   * Stops the currently selected administrative entity.
   */
  void stop(String command) throws AdminException {
    if (this.entity.getContext().isDistributedSystem()) {
      AdminDistributedSystem sys =
        (AdminDistributedSystem) this.entity.getObject();
      sys.stop();
    }
    else if (this.entity.getContext().isCacheServer()) {
      CacheVm csvr = (CacheVm) this.entity.getObject();
      csvr.stop();
    }
    else if (this.entity.getContext().isDistributionLocator()) {
      DistributionLocator loc = (DistributionLocator) this.entity.getObject();
      loc.stop();
    }
  }
  
  /**
   * Selects the <code>DistributedSystem</code> entity if it exists.
   */
  void system(String command) throws AdminException {
    if ("system".equals(command)) {
      if (hasEntity(Context.DISTRIBUTED_SYSTEM)) 
        popEntity(Context.DISTRIBUTED_SYSTEM);
    }
  }
  
  /**
   * Selects the named <code>SystemMember</code> entity if it exists.
   */
  void member(String command) throws AdminException {
    if ("member".equals(command)) {
      if (hasEntity(Context.SYSTEM_MEMBER)) popEntity(Context.SYSTEM_MEMBER);
    }
    else {
      SystemMember member = findMember(parseName(command));
      if (member != null) {
        pushEntity(Context.SYSTEM_MEMBER, member);
      }
    }
  }
  
  /**
   * Selects the indicated <code>CacheVm</code> entity if it exists.
   */
  void cacheserver(String command) throws AdminException {
    if ("cacheserver".equals(command)) {
      if (hasEntity(Context.CACHE_SERVER)) 
        popEntity(Context.CACHE_SERVER);
    }
    else {
      CacheVm cacheserver = findCacheServer(parseName(command));
      if (cacheserver != null) {
        pushEntity(Context.CACHE_SERVER, cacheserver);
      }
    }
  }
  
  /**
   * Selects the named <code>DistributionLocator</code> entity if it exists. 
   */
  void locator(String command) throws AdminException {
    if ("locator".equals(command)) {
      if (hasEntity(Context.DISTRIBUTION_LOCATOR)) 
        popEntity(Context.DISTRIBUTION_LOCATOR);
    }
    else {
      DistributionLocator locator = findLocator(parseName(command));
      if (locator != null) {
        pushEntity(Context.DISTRIBUTION_LOCATOR, locator);
      }
    }
  }
  
  /**
   * Selects the named <code>StatisticResource</code> entity if it exists.
   */
  void stat(String command) throws AdminException {
    StatisticResource stat = findStatisticResource(parseName(command));
    if (stat != null) {
      pushEntity(Context.STATISTIC_RESOURCE, stat);
    }
  }
  
  /**
   * Selects the member's SystemMemberCache.
   */
  void cache(String command) throws AdminException {
    if (hasEntity(Context.SYSTEM_MEMBER_CACHE)) {
      popEntity(Context.SYSTEM_MEMBER_CACHE);
    }
    else {
      if (this.entity.getContext().isSystemMember() || 
          this.entity.getContext().isCacheServer() ) {
        SystemMember member = (SystemMember) this.entity.getObject();
        SystemMemberCache memberCache = member.getCache();
        pushEntity(Context.SYSTEM_MEMBER_CACHE, memberCache);
      }
    }
  }
  
  /**
   * Creates the named root <code>Region</code> in the current cache.
   */
  void createRootRegion(String command) throws AdminException {
    String name = parseName(command);
      
    if (name.length() == 0) {
      System.out.println("Please provide a name for the root region");
    }
    else {
      if (this.entity.getContext().isSystemMemberCache()) {
        SystemMemberCache memberCache = (SystemMemberCache) this.entity.getObject();
        if (memberCache.isServer() == false) {
          System.out.println("Regions can only be created in cache servers.");
        }
        else {
          AttributesFactory<Object, Object> fac = new AttributesFactory<Object, Object>();
          SystemMemberRegion rgn = 
            memberCache.createRegion(name, fac.create());
          if (rgn != null) {
            pushEntity(Context.SYSTEM_MEMBER_REGION, rgn);
          }
        }
      }
      else {
        System.out.println("Please choose a cache context before creating a root region.");
      }
    }
  }
  
  /**
   * Creates the named sub<code>Region</code> of the current region.
   */
  void createSubRegion(String command) throws AdminException {
    String name = parseName(command);
      
    if (name.length() == 0) {
      System.out.println("Please provide a name for the region");
    }
    else {
      if (this.entity.getContext().isSystemMemberRegion()) {
        SystemMemberRegion currRegion = 
              (SystemMemberRegion) this.entity.getObject();
        AttributesFactory<Object, Object> fac = new AttributesFactory<Object, Object>();
        SystemMemberRegion rgn = 
              currRegion.createSubregion(name, fac.create());
        if (rgn != null) {
          pushEntity(Context.SYSTEM_MEMBER_REGION, rgn);
        }
      }
      else {
      System.out.println("Please choose a region before creating a subregion.");
      }
    }
  }
  
  /**
   * Selects the named <code>SystemMemberRegion</code> entity if it exists.
   */
  void region(String command) throws AdminException {
    String name = parseName(command);
    if (name.indexOf("..") > -1) {
      if (hasEntity(Context.SYSTEM_MEMBER_REGION)) {
        this.entity = (Entity) this.history.pop();
      }
    }
    else {
      SystemMemberRegion rgn = findSystemMemberRegion(parseName(command));
      if (rgn != null) {
        pushEntity(Context.SYSTEM_MEMBER_REGION, rgn);
      }
    }
  }
  
  // ************ Listing methods **********************************

  /**
   * Lists the details for the <code>DistributedSystem</code>.
   */
  void listDistributedSystem() {
    System.out.println("\nDistributed System Details:");
    System.out.println("\t" + "id: " + this.system.getId());
    System.out.println("\t" + "name: " + this.system.getConfig().getSystemName());
    System.out.println("\t" + "isRunning: " + this.system.isRunning());  
    if (this.system.isMcastEnabled()) {
      System.out.println("\t" + "multicast address: " + this.system.getMcastAddress());
      System.out.println("\t" + "multicast port: " + this.system.getMcastPort());
    }
    if (!this.system.isMcastDiscovery()) {
      System.out.println("\t" + "locators: " + this.system.getLocators());
    }
    System.out.println("\t" + "remote command: " + this.system.getRemoteCommand());
    System.out.println("\t" + "alert level: " + this.system.getAlertLevel());
    System.out.println("\t" + "latest alert: " + this.system.getLatestAlert());
    
    if (!this.system.isMcastDiscovery()) {
      DistributionLocator[] locs = this.system.getDistributionLocators();
      System.out.println("Distribution Locators:");
      for (int i = 0; i < locs.length; i++) {
        System.out.println("\t" + locs[i]);
      }
    }
    
    try {
      CacheVm[] cacheservers = this.system.getCacheVms();
      System.out.println("Cache Servers:");
      for (int i = 0; i < cacheservers.length; i++) {
        CacheVm cacheserver = cacheservers[i];
        System.out.println("\tname: " + cacheserver.getName() + " (Id: " +
                           cacheserver.getId() + ")");
      }
    } catch (AdminException e) {
      e.printStackTrace();
    }

    try {
      SystemMember[] apps = this.system.getSystemMemberApplications();
      System.out.println("System Member Applications:");
      for (int i = 0; i < apps.length; i++) {
        SystemMember app = apps[i];
        System.out.println("\tname: " + app.getName() + " (Id: " + app.getId() +
                           ")");
      }
    } catch (AdminException e) {
      e.printStackTrace();
    }

    System.out.println("");
  }
  
  /**
   * Lists the details for a <code>DistributionLocator</code>.
   */
  void listDistributionLocator() {
    DistributionLocator locator = (DistributionLocator) this.entity.getObject();
    System.out.println("Distribution Locator Details:");
    System.out.println("\t" + "id: " + locator.getId());
    System.out.println("\t" + "host: " + locator.getConfig().getHost());
    System.out.println("\t" + "port: " + locator.getConfig().getPort());
    System.out.println("\t" + "working directory: " + locator.getConfig().getWorkingDirectory());
    System.out.println("\t" + "product directory: " + locator.getConfig().getProductDirectory());
    System.out.println("\t" + "remote command: " + locator.getConfig().getRemoteCommand());
    System.out.println("\t" + "isRunning: " + locator.isRunning());
  }
  
  /**
   * Lists the details for a <code>CacheServer</code>.
   */
  void listCacheServer() {
    CacheVm cacheserver = (CacheVm) this.entity.getObject();
    System.out.println("Cache Server Application Details:");
    System.out.println("\t" + "id: " + cacheserver.getId());
    System.out.println("\t" + "host: " + cacheserver.getHost());
    System.out.println("\t" + "product directory: " + cacheserver.getVmConfig().getProductDirectory());
    System.out.println("\t" + "isRunning: " + cacheserver.isRunning());

    ConfigurationParameter[] configs = cacheserver.getConfiguration();
    System.out.println("Configuration Parameters:");
    for (int i = 0; i < configs.length; i++) {
      System.out.println("\t" + 
          configs[i].getName() + "=" + configs[i].getValueAsString());
    }

    try {
      StatisticResource[] stats = cacheserver.getStats();
      System.out.println("Statistic Resources:");
      for (int i = 0; i < stats.length; i++) {
        System.out.println("\t" + stats[i].getName());
      }
    } catch (AdminException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Lists the details for a <code>SystemMember</code>.
   */
  void listSystemMember() {
    SystemMember member = (SystemMember) this.entity.getObject();
    System.out.println("System Member Application Details:");
    System.out.println("\t" + "id: " + member.getId());
    System.out.println("\t" + "host: " + member.getHost());

    ConfigurationParameter[] configs = member.getConfiguration();
    System.out.println("Configuration Parameters:");
    for (int i = 0; i < configs.length; i++) {
      System.out.println("\t" + 
          configs[i].getName() + "=" + configs[i].getValueAsString());
    }

    try {
      StatisticResource[] stats = member.getStats();
      System.out.println("Statistic Resources:");
      for (int i = 0; i < stats.length; i++) {
        System.out.println("\t" + stats[i].getName());
      }
    } catch (AdminException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Lists the details for a <code>SystemMemberCache</code>.
   */
  void listSystemMemberCache() {
    SystemMemberCache memberCache = (SystemMemberCache) this.entity.getObject();
    memberCache.refresh();
    
    System.out.println("System Member Cache Details:");
    System.out.println("\t" + "id: " + memberCache.getId());
    System.out.println("\t" + "name: " + memberCache.getName());
    System.out.println("\t" + "isClosed: " + memberCache.isClosed());
    System.out.println("\t" + "lockTimeout: " + memberCache.getLockTimeout());
    System.out.println("\t" + "lockLease: " + memberCache.getLockLease());
    System.out.println("\t" + "searchTimeout: " + memberCache.getSearchTimeout());
    System.out.println("\t" + "upTime: " + memberCache.getUpTime());

    System.out.println("Root Region Names:");
    for (Object rootRegionName : memberCache.getRootRegionNames()) {
      System.out.println("\t" + rootRegionName);
    }

    System.out.println("Cache Statistics:");
    Statistic[] stats = memberCache.getStatistics();
    for (int i = 0; i < stats.length; i++) {
      System.out.println("\t" + 
          stats[i].getName() + "=" + stats[i].getValue());
    }
    /*try {
    } catch (AdminException e) {
      e.printStackTrace();
    }*/
  }
  
  /**
   * Lists the details for a <code>SystemMemberRegion</code>.
   */
  void listSystemMemberRegion() {
    SystemMemberRegion region = (SystemMemberRegion) this.entity.getObject();
    region.refresh();
    
    System.out.println("System Member Region Details:");
    System.out.println("\t" + "name: " + region.getName());
    System.out.println("\t" + "fullPath: " + region.getFullPath());
    System.out.println("\t" + "entryCount: " + region.getEntryCount());
    System.out.println("\t" + "subregionCount: " + region.getSubregionCount());

    System.out.println("Subregion Full Paths:");
    for (Object subRegionFullPath : region.getSubregionFullPaths()) {
    	System.out.println("\t" + subRegionFullPath);
    }
    
    System.out.println("Region Attributes:");
    System.out.println("\t" + "UserAttribute: " + region.getUserAttribute());
    System.out.println("\t" + "CacheLoader: " + region.getCacheLoader());
    System.out.println("\t" + "CacheWriter: " + region.getCacheWriter());
    System.out.println("\t" + "CacheListeners: " + Arrays.toString(region.getCacheListeners()));
    System.out.println("\t" + "KeyConstraint: " + region.getKeyConstraint());
    System.out.println("\t" + "RegionTimeToLiveTimeLimit: " + region.getRegionTimeToLiveTimeLimit());
    System.out.println("\t" + "RegionTimeToLiveAction: " + region.getRegionTimeToLiveAction());
    System.out.println("\t" + "EntryTimeToLiveTimeLimit: " + region.getEntryTimeToLiveTimeLimit());
    System.out.println("\t" + "EntryTimeToLiveAction: " + region.getEntryTimeToLiveAction());
    System.out.println("\t" + "CustomEntryTimeToLive: " + region.getCustomEntryTimeToLive());
    System.out.println("\t" + "RegionIdleTimeoutTimeLimit: " + region.getRegionIdleTimeoutTimeLimit());
    System.out.println("\t" + "RegionIdleTimeoutAction: " + region.getRegionIdleTimeoutAction());
    System.out.println("\t" + "EntryIdleTimeoutTimeLimit: " + region.getEntryIdleTimeoutTimeLimit());
    System.out.println("\t" + "EntryIdleTimeoutAction: " + region.getEntryIdleTimeoutAction());
    System.out.println("\t" + "CustomEntryIdleTimeout: " + region.getCustomEntryIdleTimeout());
    System.out.println("\t" + "DataPolicy: " + region.getDataPolicy());
    System.out.println("\t" + "Scope: " + region.getScope());
    System.out.println("\t" + "SubscriptionAttributes.InterestPolicy: " + region.getSubscriptionAttributes().getInterestPolicy());
    System.out.println("\t" + "InitialCapacity: " + region.getInitialCapacity());
    System.out.println("\t" + "LoadFactor: " + region.getLoadFactor());
    System.out.println("\t" + "ConcurrencyLevel: " + region.getConcurrencyLevel());
    System.out.println("\t" + "StatisticsEnabled: " + region.getStatisticsEnabled());
    
    System.out.println("Region Statistics:");
    System.out.println("\t" + "LastModifiedTime: " + region.getLastModifiedTime());
    System.out.println("\t" + "LastAccessedTime: " + region.getLastAccessedTime());
    System.out.println("\t" + "HitCount: " + region.getHitCount());
    System.out.println("\t" + "MissCount: " + region.getMissCount());
    System.out.println("\t" + "HitRatio: " + region.getHitRatio());
  }
  
  /**
   * Lists the details for a <code>StatisticResource</code>.
   */
  void listStatisticResource() {
    StatisticResource statResource = 
        (StatisticResource) this.entity.getObject();

    try {
      statResource.refresh();
    }
    catch (AdminException e) {
      e.printStackTrace();
    }
    
    System.out.println("Statistic Resource Stats:");
    Statistic[] stats = statResource.getStatistics();
    for (int i = 0; i < stats.length; i++) {
      System.out.println("\t" + 
          stats[i].getName() + "=" + stats[i].getValue());
    }
  }
  
  // ************ Finder methods **********************************

  /**
   * Finds the named <code>SystemMemberRegion</code> under the current context.
   */
  SystemMemberRegion findSystemMemberRegion(String name) throws AdminException {
    if (name == null) return null;
    
    if (this.entity.getContext().isSystemMemberCache()) {
      SystemMemberCache memberCache = (SystemMemberCache) this.entity.getObject();
      SystemMemberRegion rgn = memberCache.getRegion(name);
      if (rgn != null) {
        this.cache = memberCache;
        return rgn;
      }
    }
    
    if (this.entity.getContext().isSystemMemberRegion()) {
      SystemMemberRegion parent = 
          (SystemMemberRegion) this.entity.getObject();
      SystemMemberRegion rgn = this.cache.getRegion(name);
      if (rgn == null) {
        rgn = this.cache.getRegion(parent.getName() + "/" + name);
      }
      return rgn;
    }
    
    return null;
  }
  
  /**
   * Finds the named <code>StatisticResource</code> under the current context.
   */
  StatisticResource findStatisticResource(String name) throws AdminException {
    if (name == null) return null;
    if (this.entity.getContext().isSystemMember() || 
        this.entity.getContext().isCacheServer()) {
      SystemMember member = (SystemMember) this.entity.getObject();
      StatisticResource[] stats = member.getStats();
      for (int i = 0; i < stats.length; i++) {
        if (name.equals(stats[i].getName())) {
          return stats[i];
        }
      }
    }
    return null;
  }
  
  /**
   * Finds the named <code>SystemMember</code> under the current context.
   */
  SystemMember findMember(String name) throws AdminException {
    if (name == null) return null;
    SystemMember[] apps = this.system.getSystemMemberApplications();
    for (int i = 0; i < apps.length; i++) {
      if (name.equals(apps[i].getName())) return apps[i];
    }
    return null;
  }
 
  /**
   * Finds the named <code>CacheServer</code> under the current context.
   */
  CacheVm findCacheServer(String name) throws AdminException {
    if (name == null) return null;
    CacheVm[] csvrs = this.system.getCacheVms();
    for (int i = 0; i < csvrs.length; i++) {
      if (name.equals(csvrs[i].getName())) return csvrs[i];
    }
    return null;
  }
 
  /**
   * Finds the named <code>DistributionLocator</code> under the current context.
   */
  DistributionLocator findLocator(String id) throws AdminException {
    if (id == null) return null;
    DistributionLocator[] locs = this.system.getDistributionLocators();
    for (int i = 0; i < locs.length; i++) {
      if (id.equals(locs[i].getId())) return locs[i];
    }
    return null;
  }
 
  // ************ Entity history methods *****************************
  
  /**
   * Sets the current entity and pushes it into the history stack.
   */
  private void pushEntity(Context context, Object object) {
    if (this.entity != null) this.history.push(this.entity);
    this.entity = new Entity(context, object);
  }
  
//  /**
//   * Pops the history stack and sets the current entity.
//   */
//  private void popEntity() {
//    if (!this.history.empty()) {
//      this.entity = (Entity) this.history.pop();
//    }
//  }
  
  /**
   * Searches the history stack for the specified context and sets the 
   * current entity if it's found.
   */
  private void popEntity(Context context) {
    while (!this.history.empty() && !this.entity.getContext().equals(context)) {
      this.entity = (Entity) this.history.pop();
    }
  }
  
  /**
   * Returns true if the history stack contains an entity matching the context.
   */
  private boolean hasEntity(Context context) {
    if (!this.history.empty()) {
      for (Entity entity : this.history) {
        if (entity.getContext().equals(context)) return true;
      }
    }
    return false;
  }
  
  // ************ Parsing methods **********************************

  /**
   * Parses a <code>command</code> and expects that the second token
   * is a name.
   */
  private String parseName(String command) {
    int space = command.indexOf(' ');
    if (space < 0) {
      System.err.println("You need to give a name argument for this command");
      return null;
    }
    else {
      int space2 = command.indexOf(' ', space+1);
      if (space2 < 0)
        return command.substring(space+1);
      else
        return command.substring(space+1, space2);
    }
  }

//  /**
//   * Parses a <code>command</code> and expects that the third token is
//   * a value.
//   */
//  private String parseValue(String command) {
//    int space = command.indexOf(' ');
//    if (space < 0) {
//      System.err.println("You need to give a value for this command");
//      return null;
//    }
//    space = command.indexOf(' ', space+1);
//    if (space < 0) {
//      System.err.println("You need to give a value for this command");
//      return null;
//    }
//    else {
//      int space2 = command.indexOf(' ', space+1);
//      if (space2 < 0)
//        return command.substring(space+1);
//      else
//        return command.substring(space+1, space2);
//    }
//  }

//  /**
//   * Parses an <code>int</code> from a <code>String</code>
//   */
//  private int parseInt(String value) {
//    try {
//      return Integer.parseInt(value);
//    }
//    catch (Exception e) {
//      System.err.println("illegal number: " + value);
//      return -1;
//    }
//  }

//  /**
//   * Parses a <code>command</code> and places each of its tokens in a
//   * <code>List</code>.
//   */
//  private boolean parseCommand(String command, List list) {
////    String strTemp = command;
//    boolean done = false;
//    boolean success = false;
//    int space = -1;
//    do {
//      space = command.indexOf(' ');
//      if (space < 0) {
//        done= true;
//        list.add(command);
//        break;
//      }
//      String str = command.substring(0,space);
//      list.add(str);
//      command = command.substring(space+1, command.length()); 
//      success = true;
//    } while(!done);
//    return success;
//  }

  // ************ inner class for Entity **************************
  
  /**
   * Container for an administrative entity.
   */
  private static class Entity {
    private final Context context;
    private final Object object;
    Entity(Context context, Object object) {
      this.context = context;
      this.object = object;
    }
    Context getContext() { return this.context; }
    Object getObject() { return this.object; }
  }
  
  // ************ inner class for Context **************************
   
  /**
   * Administrative entity context type.
   */
  private static class Context implements java.io.Serializable {
    
    public static final Context DISTRIBUTED_SYSTEM = new Context("DistributedSystem", "system");
    public static final Context DISTRIBUTION_LOCATOR = new Context("DistributionLocator", "locator");
    public static final Context SYSTEM_MEMBER = new Context("SystemMember", "member");
    public static final Context CACHE_SERVER = new Context("CacheServer", "cacheserver");
    public static final Context STATISTIC_RESOURCE = new Context("StatisticResource", "stat");
    public static final Context SYSTEM_MEMBER_CACHE = new Context("SystemMemberCache", "cache");
    public static final Context SYSTEM_MEMBER_REGION = new Context("SystemMemberRegion", "region");
  
    /** The display-friendly name of this Context type. */
    private final transient String name;
    
    /** The prompt prefix for this Context type. */
    private final transient String prompt;
    
    // The 4 declarations below are necessary for serialization
    /** int used as ordinal to represent this Scope */
    public final int ordinal = nextOrdinal++;
  
    private static int nextOrdinal = 0;
    
    private static final Context[] VALUES =
      { DISTRIBUTED_SYSTEM, DISTRIBUTION_LOCATOR, SYSTEM_MEMBER,
        CACHE_SERVER, STATISTIC_RESOURCE, SYSTEM_MEMBER_CACHE, 
        SYSTEM_MEMBER_REGION };
  
    private Object readResolve() throws java.io.ObjectStreamException {
      return VALUES[ordinal];  // Canonicalize
    }
    
    /** Creates a new instance of Context. */
    private Context(String name, String prompt) {
      this.name = name;
      this.prompt = prompt;
    }
      
    /** Return the Context represented by specified ordinal */
    public static Context fromOrdinal(int ordinal) {
      return VALUES[ordinal];
    }
  
    public String getName() {
      return this.name;
    }
    
    public String getPrompt() {
      return this.prompt;
    }
    
    /** Return whether this is <code>DISTRIBUTED_SYSTEM</code>. */
    public boolean isDistributedSystem() {
      return this.equals(DISTRIBUTED_SYSTEM);
    }
      
    /** Return whether this is <code>DISTRIBUTION_LOCATOR</code>. */
    public boolean isDistributionLocator() {
      return this.equals(DISTRIBUTION_LOCATOR);
    }
      
    /** Return whether this is <code>SYSTEM_MEMBER</code>. */
    public boolean isSystemMember() {
      return this.equals(SYSTEM_MEMBER);
    }
      
    /** Return whether this is <code>CACHE_SERVER</code>. */
    public boolean isCacheServer() {
      return this.equals(CACHE_SERVER);
    }
      
    /** Return whether this is <code>STATISTIC_RESOURCE</code>. */
    public boolean isStatisticResource() {
      return this.equals(STATISTIC_RESOURCE);
    }
      
    /** Return whether this is <code>SYSTEM_MEMBER_CACHE</code>. */
    public boolean isSystemMemberCache() {
      return this.equals(SYSTEM_MEMBER_CACHE);
    }
      
    /** Return whether this is <code>SYSTEM_MEMBER_REGION</code>. */
    public boolean isSystemMemberRegion() {
      return this.equals(SYSTEM_MEMBER_REGION);
    }
      
    /** 
     * Returns a string representation for this system member type.
     *
     * @return the name of this system member type
     */
    @Override
    public String toString() {
        return this.prompt;
    }
  
    /**history
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  other  the reference object with which to compare.
     * @return true if this object is the same as the obj argument;
     *         false otherwise.
     */
    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (other == null) return false;
      if (!(other instanceof SystemMemberType)) return  false;
      final SystemMemberType that = (SystemMemberType) other;
      if (this.ordinal != that.ordinal) return false;
      return true;
    }
  
    /**
     * Returns a hash code for the object. This method is supported for the
     * benefit of hashtables such as those provided by java.util.Hashtable.
     *
     * @return the integer 0 if description is null; otherwise a unique integer.
     */
    @Override
    public int hashCode() {
      int result = 17;
      final int mult = 37;
      result = mult * result + this.ordinal;
      return result;
    }
  
  }
  
}
