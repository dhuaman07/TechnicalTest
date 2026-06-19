package com.dhuaman.application.shared;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryBusImpl implements QueryBus {

    private final List<QueryHandler<?, ?>> handlers;

    public QueryBusImpl(List<QueryHandler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Object query) {
        QueryHandler<Object, R> handler = (QueryHandler<Object, R>) handlers.stream()
                .filter(h -> resolveQueryType(h) == query.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No handler found for query: " + query.getClass().getSimpleName()));
        return handler.handle(query);
    }

    private Class<?> resolveQueryType(QueryHandler<?, ?> handler) {
        return ResolvableType.forClass(handler.getClass())
                .as(QueryHandler.class)
                .getGeneric(0)
                .resolve();
    }
}
