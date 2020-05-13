package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Slot;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Slot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    /**
     *
     * @param type
     * @param status
     * @return
     */
    @Query("SELECT s FROM Slot s where s.type =:type and s.status =:status") 
    List<Slot> findSlotByType(@Param("type") Enum type, @Param("status") Enum status);
}
