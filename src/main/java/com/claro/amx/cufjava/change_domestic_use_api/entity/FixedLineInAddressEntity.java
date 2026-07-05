package com.claro.amx.cufjava.change_domestic_use_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedNativeQuery(
        name = "FixedLineInAddressEntity.getFixedLinesInAddress",
        query = "SELECT c.clu_cellular_number, c.clu_cbt_id, c.clu_status" +
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
        resultClass = FixedLineInAddressEntity.class
)
public class FixedLineInAddressEntity {

    @Id
    @Column(name = "clu_cellular_number")
    private String lineNumber;

    @Column(name = "clu_cbt_id")
    private String businessType;

    @Column(name = "clu_status")
    private String status;
}
