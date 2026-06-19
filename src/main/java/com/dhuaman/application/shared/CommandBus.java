package com.dhuaman.application.shared;

public interface CommandBus {
    <R> R dispatch(Object command);
}
