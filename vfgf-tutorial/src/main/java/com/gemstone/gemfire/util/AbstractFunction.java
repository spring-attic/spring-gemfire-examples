package com.gemstone.gemfire.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.execute.ResultSender;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.internal.DefaultQuery;
import com.gemstone.gemfire.cache.query.internal.ResultsBag;
import com.gemstone.gemfire.internal.cache.LocalDataSet;


public abstract class AbstractFunction implements Function, Declarable {

    private boolean useQuery=true;
    private Region region;
    private Cache cache;

    public void init(Properties arg0) {
    }

    public void execute(FunctionContext ctx) {
        if (useQuery) {
            executeUsingLocalQuery(ctx);
        } else {
            executeUsingLocalLoop(ctx);
        }
    }

    protected abstract void executeUsingLocalQuery(FunctionContext ctx);
    protected abstract void executeUsingLocalLoop(FunctionContext ctx);

    public String getId() {
        return getClass().getName();
    }

    public boolean hasResult() {
        return true;
    }

    public boolean isHA() {
        return true;
    }

    public boolean optimizeForWrite() {
        return false;
    }

    public boolean isUseQuery() {
        return useQuery;
    }

    public void setUseQuery(boolean useQuery) {
        this.useQuery = useQuery;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }


    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

}
