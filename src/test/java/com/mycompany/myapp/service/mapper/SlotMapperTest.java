package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SlotMapperTest {

    private SlotMapper slotMapper;

    @BeforeEach
    public void setUp() {
        slotMapper = new SlotMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(slotMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(slotMapper.fromId(null)).isNull();
    }
}
