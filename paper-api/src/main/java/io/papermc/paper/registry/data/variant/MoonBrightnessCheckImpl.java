package io.papermc.paper.registry.data.variant;

import com.google.common.collect.Range;

record MoonBrightnessCheckImpl(Range<Double> range) implements SpawnCondition.MoonBrightnessCheck {
}
