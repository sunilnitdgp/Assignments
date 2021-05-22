package com.assignment.repository;

import com.assignment.model.MedicinesDetails;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MedicinesDetailsRepository extends BaseRepository{
    private static final String[] ID_ARRAY = { "c_unique_id" };


    private static final String SAVE_MEDICINE_DETAILS = "INSERT INTO medicines_details(c_name, c_batch_no, d_expiry_date," +
            "n_balance_qty, c_unique_code, c_schemes, n_mrp, c_manufacturer, hsn_code, c_packaging) VALUES (:c_name, " +
            ":c_batch_no, :d_expiry_date, :n_balance_qty, :c_unique_code, :c_schemes, :n_mrp, :c_manufacturer, " +
            ":hsn_code, :c_packaging)";

    private static final String FIND_ALL_BY_ID = "SELECT * FROM medicines_details WHERE c_unique_id = :c_unique_id";

    private static final String SEARCH_MEDICINES = "SELECT c_name FROM medicines_details WHERE LOWER(c_name) LIKE " +
            ":c_name";

    public MedicinesDetails save(MedicinesDetails medicinesDetails) {
        medicinesDetails.setC_unique_id(saveAndGetPrimaryKey(SAVE_MEDICINE_DETAILS, getInput(medicinesDetails), ID_ARRAY));
        return medicinesDetails;
    }

    public List<String> searchMedicines(String searchTag) {
        searchTag = searchTag.trim().toLowerCase() + "%";
        return findAll(SEARCH_MEDICINES, String.class, Map.of(C_NAME, searchTag));
    }

    public MedicinesDetails getMedicineDetail(Long id) {
        return find(FIND_ALL_BY_ID, MedicinesDetails.class, Map.of(C_UNIQUE_ID, id));
    }

    private Map<String, Object> getInput(MedicinesDetails medicinesDetails) {
        Map<String, Object> input = new HashMap<>();
        input.put(C_NAME, medicinesDetails.getC_name());
        input.put(C_BATCH_NO, medicinesDetails.getC_batch_no());
        input.put(N_BALANCE_QTY, medicinesDetails.getN_balance_qty());
        input.put(C_PACKAGING, medicinesDetails.getC_packaging());
        input.put(D_EXPIRY_DATE, medicinesDetails.getD_expiry_date());
        input.put(C_UNIQUE_CODE, medicinesDetails.getC_unique_code());
        input.put(C_SCHEMES, medicinesDetails.getC_schemes());
        input.put(N_MRP, medicinesDetails.getN_mrp());
        input.put(C_MANUFACTURER, medicinesDetails.getC_manufacturer());
        input.put(HSN_CODE, medicinesDetails.getHsn_code());
        return input;
    }

}
