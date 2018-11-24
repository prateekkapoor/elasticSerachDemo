package com.kapoopr.elasticSearch.demo.services.file.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kapoopr.elasticSearch.demo.exceptions.FileParserException;
import com.kapoopr.elasticSearch.demo.util.FileUtil;

/**
 * This Class is used to parse the excel file
 */
@Component
@Scope("prototype")
public class ExcelFileParser {
    List<Object> list = new ArrayList<Object>();
    private int columns;
    private int items;
    @Autowired
    FileUtil fileUtil;

    /**
     * Parses the contents of an XLSX into JSON.
     *
     * @param bytes  bytes of an XLSX file.
     * @param offset row number containing column titles.
     */
    public ExcelFileParser() {

    }

    public String toJsonArray(String name) {
        Map<String, Object> filesMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        try {
            filesMap.put(name, list.toArray());
            jsonString = mapper.writeValueAsString(filesMap);
        } catch (IOException e) {
            jsonString = "fail";
        }
        return jsonString;
    }

    private void excelToString(byte[] bytes, int offset) throws FileParserException {
        XSSFWorkbook workbook = null;
        offset -= 1; // convert excel row name to index.
        try {
            workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
            XSSFSheet sheet = workbook.getSheetAt(0);

            this.columns = getColumnCount(sheet.getRow(offset));
            this.items = getItemCount(sheet, offset);

            readRows(sheet, offset);
        } catch (Exception e) {
            throw new FileParserException(e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readRows(XSSFSheet sheet, int columnRow) {
        String[] columns = getColumns(sheet.getRow(columnRow));

        for (int i = 0; i < items; i++) {
            list.add(getRow(columns, sheet.getRow(i + columnRow + 1)));
        }
    }

    private String[] getColumns(XSSFRow row) {
        String[] titles = new String[columns];

        for (int i = 0; i < titles.length; i++) {
            titles[i] = row.getCell(i).getStringCellValue();
        }
        return titles;
    }

    private int getColumnCount(XSSFRow row) {
        Iterator<Cell> iterator = row.iterator();
        int count = 0;

        while (iterator.hasNext()) {
            Cell cell = iterator.next();

            if (cell.getStringCellValue() != null) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private int getItemCount(XSSFSheet sheet, int offset) {
        int count = 0;
        Row row = sheet.getRow(offset + 1);

        while (row != null) {
            count++;
            row = sheet.getRow(offset + 1 + count);
        }

        return count;
    }

    private Map<String, Object> getRow(String[] titles, XSSFRow row) {
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        int index = 0;

        for (Cell cell : row) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    fileInfo.put(titles[index], cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    fileInfo.put(titles[index], cell.getStringCellValue().split("/")[0]);
                    break;
            }
            index++;
        }
        return fileInfo;
    }

    public String excelToJson(String file, String name) throws IOException, FileParserException {
        byte[] fileBytes = fileUtil.fileToBytes(file);
        excelToString(fileBytes, 1);
        return toJsonArray(name);

    }
}
