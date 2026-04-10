package com.potentia.automation.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExcelReportListener implements ITestListener {

    private static final String REPORT_DIR =
            System.getProperty("user.dir") + File.separator + "test-output";
    private static final String REPORT_FILE =
            REPORT_DIR + File.separator + "TestResults.xlsx";
    private static final String SHEET_NAME = "Execution Results";

    private int currentRowNum = 1;

    @Override
    public void onStart(ITestContext context) {
        createFreshReportFile();   // always create new file for every run
        currentRowNum = 1;
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeResult(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeResult(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeResult(result, "SKIPPED");
    }

    private synchronized void writeResult(ITestResult result, String status) {
        FileInputStream fis = null;
        Workbook workbook = null;
        FileOutputStream fos = null;

        try {
            File file = new File(REPORT_FILE);
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(SHEET_NAME);

            Row row = sheet.createRow(currentRowNum++);

            String testCaseName = result.getMethod().getMethodName();
            String className = result.getTestClass().getName();
            String testName = result.getMethod().getDescription() != null
                    ? result.getMethod().getDescription()
                    : testCaseName;

            long startMillis = result.getStartMillis();
            long endMillis = result.getEndMillis();
            long duration = endMillis - startMillis;

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            row.createCell(0).setCellValue(currentRowNum - 1);
            row.createCell(1).setCellValue(testCaseName);
            row.createCell(2).setCellValue(className);
            row.createCell(3).setCellValue(testName);
            row.createCell(4).setCellValue(status);
            row.createCell(5).setCellValue(duration + " ms");
            row.createCell(6).setCellValue(timestamp);

            String errorMessage = "";
            if (result.getThrowable() != null) {
                errorMessage = result.getThrowable().getMessage();
                if (errorMessage == null) {
                    errorMessage = result.getThrowable().toString();
                }
            }
            row.createCell(7).setCellValue(errorMessage);

            applyStatusStyle(workbook, row.getCell(4), status);
            autoSizeColumns(sheet, 8);

            closeQuietly(fis);
            fos = new FileOutputStream(file);
            workbook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(fis);
            closeQuietly(fos);
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createFreshReportFile() {
        Workbook workbook = null;
        FileOutputStream fos = null;

        try {
            File dir = new File(REPORT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Test Case ID / Method");
            header.createCell(2).setCellValue("Class Name");
            header.createCell(3).setCellValue("Test Description");
            header.createCell(4).setCellValue("Status");
            header.createCell(5).setCellValue("Execution Time");
            header.createCell(6).setCellValue("Executed On");
            header.createCell(7).setCellValue("Failure Reason");

            styleHeader(workbook, header);
            autoSizeColumns(sheet, 8);

            fos = new FileOutputStream(REPORT_FILE, false); // overwrite old file
            workbook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(fos);
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void styleHeader(Workbook workbook, Row header) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        for (Cell cell : header) {
            cell.setCellStyle(style);
        }
    }

    private void applyStatusStyle(Workbook workbook, Cell cell, String status) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);

        switch (status.toUpperCase()) {
            case "PASS":
                font.setColor(IndexedColors.GREEN.getIndex());
                break;
            case "FAIL":
                font.setColor(IndexedColors.RED.getIndex());
                break;
            case "SKIPPED":
                font.setColor(IndexedColors.ORANGE.getIndex());
                break;
            default:
                break;
        }

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet, int totalColumns) {
        for (int i = 0; i < totalColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}