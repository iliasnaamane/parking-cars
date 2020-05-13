package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class SlotDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotDTO.class);
        SlotDTO slotDTO1 = new SlotDTO();
        slotDTO1.setId(1L);
        SlotDTO slotDTO2 = new SlotDTO();
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO2.setId(slotDTO1.getId());
        assertThat(slotDTO1).isEqualTo(slotDTO2);
        slotDTO2.setId(2L);
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO1.setId(null);
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
    }
}
