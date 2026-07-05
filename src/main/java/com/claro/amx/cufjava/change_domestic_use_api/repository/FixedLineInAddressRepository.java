package com.claro.amx.cufjava.change_domestic_use_api.repository;

import com.claro.amx.cufjava.change_domestic_use_api.entity.FixedLineInAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedLineInAddressRepository extends JpaRepository<FixedLineInAddressEntity, String> {

    /**
     * Retrieves all IF/TF/IPTV lines in the same address as the given line number,
     * belonging to the given account, and not in status 'I' or 'C'.
     *
     * @param accountId  the account identifier
     * @param lineNumber the reference line number to determine the address
     * @return list of fixed lines in the same address
     */
    @Query(value = "SELECT c.clu_cellular_number, c.clu_cbt_id, c.clu_status" +
            " FROM cellulars c, cellular_address ca" +
            " WHERE c.clu_cellular_number = ca.cad_clu_cellular_number" +
            "   AND c.clu_acc_id = :accountId" +
            "   AND c.clu_cbt_id IN ('IF', 'TF', 'IPTV')" +
            "   AND c.clu_status NOT IN ('I', 'C')" +
            "   AND ca.cad_aaa_id = (" +
            "       SELECT ca2.cad_aaa_id FROM cellular_address ca2" +
            "       WHERE ca2.cad_clu_cellular_number = :lineNumber" +
            "         AND NVL(ca2.cad_start_date, SYSDATE - 1) <= SYSDATE" +
            "         AND NVL(ca2.cad_end_date, SYSDATE + 1) > SYSDATE" +
            "         AND ROWNUM = 1)",
            nativeQuery = true)
    List<FixedLineInAddressEntity> getFixedLinesInAddress(@Param("accountId") String accountId,
                                                          @Param("lineNumber") String lineNumber);
}
