package com.thoughtworks.webstub.server.utils;

import com.thoughtworks.webstub.utils.Predicate;
import com.thoughtworks.webstub.utils.PredicatedPartition;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;

import java.util.Collection;

import static com.thoughtworks.webstub.utils.CollectionUtils.partition;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.ArrayUtils.contains;

public class JettyFilterRemover extends JettyHandlerRemover<FilterMapping, FilterHolder> {
    public JettyFilterRemover(org.eclipse.jetty.servlet.ServletContextHandler contextHandler) {
        super(contextHandler);
    }

    @Override
    PredicatedPartition<FilterMapping> partitionMappingsBy(final String pathSpec) {
        FilterMapping[] filterMappings = servletHandler().getFilterMappings();
        if (filterMappings == null)
            return PredicatedPartition.empty();

        return partition(asList(filterMappings), new Predicate<FilterMapping>() {
            @Override
            public boolean satisfies(FilterMapping mapping) {
                return contains(mapping.getPathSpecs(), pathSpec);
            }
        });
    }

    @Override
    void setNewMappings(Collection<FilterMapping> mappings) {
        servletHandler().setFilterMappings(mappings.toArray(new FilterMapping[]{}));
    }

    @Override
    PredicatedPartition<FilterHolder> partitionHoldersBy(final Collection<String> handlerNames) {
        return partition(asList(servletHandler().getFilters()), new Predicate<FilterHolder>() {
            @Override
            public boolean satisfies(FilterHolder holder) {
                return handlerNames.contains(holder.getName());
            }
        });
    }

    @Override
    void setNewHolders(Collection<FilterHolder> holders) {
        servletHandler().setFilters(holders.toArray(new FilterHolder[]{}));
    }

    @Override
    String getHandlerName(FilterMapping mapping) {
        return mapping.getFilterName();
    }
}
