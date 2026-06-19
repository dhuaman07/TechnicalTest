package com.dhuaman.application.shared;

public interface QueryBus {
    <R> R dispatch(Object query);
}
