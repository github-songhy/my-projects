package com.earlywarning.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        return DbContextHolder.getDatabaseType();
    }
}
