package com.assignment.controller;

import com.assignment.model.MedicinesDetails;
import com.assignment.repository.MedicinesDetailsRepository;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class MedicinesDetailsController {

    @Autowired private MedicinesDetailsRepository medicinesDetailsRepository;

    @PostMapping("/uploadCsv")
    public ResponseEntity<List<MedicinesDetails>> saveMedicinesDetails(@RequestBody MultipartFile medicinesFile)
            throws IOException, ParseException {
        Reader reader = new InputStreamReader(medicinesFile.getInputStream());
        CSVReaderHeaderAware csvReaderHeaderAware = new CSVReaderHeaderAware(reader);
        List<Map<String, String>> rawCsvData = readAllAsMap(csvReaderHeaderAware);
        List<MedicinesDetails> medicinesDetails = new ArrayList<>();
        for(Map<String, String> csvData : rawCsvData) {
            MedicinesDetails medicinesDetail = new MedicinesDetails();
            for(Map.Entry<String, String> entry : csvData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case MedicinesDetailsRepository.C_NAME:
                        medicinesDetail.setC_name(value);
                        break;
                    case MedicinesDetailsRepository.C_BATCH_NO:
                        medicinesDetail.setC_batch_no(value);
                        break;
                    case MedicinesDetailsRepository.D_EXPIRY_DATE:
                        Date expiryDate =new SimpleDateFormat("dd/MM/yyyy").parse(value);
                        medicinesDetail.setD_expiry_date(expiryDate);
                        break;
                    case MedicinesDetailsRepository.N_BALANCE_QTY:
                        medicinesDetail.setN_balance_qty(Integer.parseInt(value));
                        break;
                    case MedicinesDetailsRepository.C_PACKAGING:
                        medicinesDetail.setC_packaging(value);
                        break;
                    case MedicinesDetailsRepository.C_UNIQUE_CODE:
                        medicinesDetail.setC_unique_code(value);
                        break;
                    case MedicinesDetailsRepository.C_SCHEMES:
                        medicinesDetail.setC_schemes(value);
                        break;
                    case MedicinesDetailsRepository.N_MRP:
                        medicinesDetail.setN_mrp(Float.parseFloat(value));
                        break;
                    case MedicinesDetailsRepository.C_MANUFACTURER:
                        medicinesDetail.setC_manufacturer(value);
                        break;
                    case MedicinesDetailsRepository.HSN_CODE:
                        medicinesDetail.setHsn_code(Long.parseLong(value));
                        break;
                }
            }
            medicinesDetails.add(medicinesDetail);
        }
        for(MedicinesDetails detail : medicinesDetails) {medicinesDetailsRepository.save(detail);
        }
        return ResponseEntity.ok(medicinesDetails);
    }

    @GetMapping("/searchMedicine")
    private ResponseEntity<List<String>> searchMedicines(@RequestParam(name = "medicine") String medicine) {
        List<String> searchResults = medicinesDetailsRepository.searchMedicines(medicine);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/getMedicineDetails/{c_unique_id}")
    private ResponseEntity<MedicinesDetails> getMedicineDetails(@PathVariable(name = "c_unique_id") Long id) {
        MedicinesDetails medicinesDetail = medicinesDetailsRepository.getMedicineDetail(id);
        return ResponseEntity.ok(medicinesDetail);
    }

    private List<Map<String, String>> readAllAsMap(CSVReaderHeaderAware reader)
            throws IOException {
        List<Map<String, String>> csvData = new ArrayList<>();
        try (reader) {
            String[] line;
            while ((line = reader.peek()) != null) {
                if ((line == null || (line.length == 1 && line[0].trim().isEmpty())))
                    reader.skip(1);   // skip empty/blank line
                else
                    csvData.add(reader.readMap());
            }
        } catch (CsvException e) {
            System.out.println("Parsing Failed!");
        }
        return csvData;
    }


}
