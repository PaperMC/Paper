package io.papermc.paper.math.provider;

record WeightedIntProviderImpl(int weight, IntProvider provider) implements WeightedIntProvider {
}
