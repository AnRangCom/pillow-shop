package com.exe201.pillow.domain;

import static com.exe201.pillow.domain.DefaultSizeTestSamples.*;
import static com.exe201.pillow.domain.PillowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exe201.pillow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DefaultSizeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DefaultSize.class);
        DefaultSize defaultSize1 = getDefaultSizeSample1();
        DefaultSize defaultSize2 = new DefaultSize();
        assertThat(defaultSize1).isNotEqualTo(defaultSize2);

        defaultSize2.setId(defaultSize1.getId());
        assertThat(defaultSize1).isEqualTo(defaultSize2);

        defaultSize2 = getDefaultSizeSample2();
        assertThat(defaultSize1).isNotEqualTo(defaultSize2);
    }

    @Test
    void pillowTest() {
        DefaultSize defaultSize = getDefaultSizeRandomSampleGenerator();
        Pillow pillowBack = getPillowRandomSampleGenerator();

        defaultSize.setPillow(pillowBack);
        assertThat(defaultSize.getPillow()).isEqualTo(pillowBack);

        defaultSize.pillow(null);
        assertThat(defaultSize.getPillow()).isNull();
    }
}
