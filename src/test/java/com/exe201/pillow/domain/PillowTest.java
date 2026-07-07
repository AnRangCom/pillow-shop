package com.exe201.pillow.domain;

import static com.exe201.pillow.domain.DefaultSizeTestSamples.*;
import static com.exe201.pillow.domain.PillowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.exe201.pillow.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PillowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pillow.class);
        Pillow pillow1 = getPillowSample1();
        Pillow pillow2 = new Pillow();
        assertThat(pillow1).isNotEqualTo(pillow2);

        pillow2.setId(pillow1.getId());
        assertThat(pillow1).isEqualTo(pillow2);

        pillow2 = getPillowSample2();
        assertThat(pillow1).isNotEqualTo(pillow2);
    }

    @Test
    void defaultSizeTest() {
        Pillow pillow = getPillowRandomSampleGenerator();
        DefaultSize defaultSizeBack = getDefaultSizeRandomSampleGenerator();

        pillow.addDefaultSize(defaultSizeBack);
        assertThat(pillow.getDefaultSizes()).containsOnly(defaultSizeBack);
        assertThat(defaultSizeBack.getPillow()).isEqualTo(pillow);

        pillow.removeDefaultSize(defaultSizeBack);
        assertThat(pillow.getDefaultSizes()).doesNotContain(defaultSizeBack);
        assertThat(defaultSizeBack.getPillow()).isNull();

        pillow.defaultSizes(new HashSet<>(Set.of(defaultSizeBack)));
        assertThat(pillow.getDefaultSizes()).containsOnly(defaultSizeBack);
        assertThat(defaultSizeBack.getPillow()).isEqualTo(pillow);

        pillow.setDefaultSizes(new HashSet<>());
        assertThat(pillow.getDefaultSizes()).doesNotContain(defaultSizeBack);
        assertThat(defaultSizeBack.getPillow()).isNull();
    }
}
