package com.dhuaman.application.shared;

public interface CommandHandler<C, R> {
    R handle(C command);
}
