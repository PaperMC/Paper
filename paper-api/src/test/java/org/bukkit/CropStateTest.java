package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;

public class CropStateTest {
    @Test
    public void getByData() {
        for (CropState cropState : CropState.values()) {
            assertThat(CropState.getByData(cropState.getData()), is(cropState));
        }
    }
}
