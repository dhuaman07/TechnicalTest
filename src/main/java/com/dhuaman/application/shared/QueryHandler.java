package com.dhuaman.application.shared;

public interface QueryHandler<Q, R> {
    R handle(Q query);
}
