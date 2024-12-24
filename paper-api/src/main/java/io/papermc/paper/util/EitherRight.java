package io.papermc.paper.util;

public record EitherRight<L, R>(R value) implements Either.Right<L, R> {
}
