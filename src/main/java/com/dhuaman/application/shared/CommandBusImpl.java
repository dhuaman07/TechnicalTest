package com.dhuaman.application.shared;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandBusImpl implements CommandBus {

    private final List<CommandHandler<?, ?>> handlers;

    public CommandBusImpl(List<CommandHandler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Object command) {
        CommandHandler<Object, R> handler = (CommandHandler<Object, R>) handlers.stream()
                .filter(h -> resolveCommandType(h) == command.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No handler found for command: " + command.getClass().getSimpleName()));
        return handler.handle(command);
    }

    private Class<?> resolveCommandType(CommandHandler<?, ?> handler) {
        return ResolvableType.forClass(handler.getClass())
                .as(CommandHandler.class)
                .getGeneric(0)
                .resolve();
    }
}
