package com.dhuaman.application.shared;

public interface CommandValidator<C> {
    void validate(C command);
}
