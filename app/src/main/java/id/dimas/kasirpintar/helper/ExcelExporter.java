package id.dimas.kasirpintar.helper;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import id.dimas.kasirpintar.model.Buy;
import id.dimas.kasirpintar.model.Categories;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.ReportTrxItem;
import id.dimas.kasirpintar.model.ReportTrxItemStock;

public class ExcelExporter {
    public static void exportToExcelTrx(Context context, List<Orders> ordersList, String fileName) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        Orders orders = new Orders();
        // Get the class of the object
        Class<?> objectClass = orders.getClass();

        // Get all fields of the class
        Field[] fields = objectClass.getDeclaredFields();

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Iterate through the fields and print their names
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fieldName);
        }


        // Create data rows
        for (int i = 0; i < ordersList.size(); i++) {
            Orders order = ordersList.get(i);
            Row dataRow = sheet.createRow(i + 1); // Adjust index to start from row 1

            for (int j = 0; j < fields.length; j++) {
                Cell cell = dataRow.createCell(j);

                try {
                    // Use reflection to get the value of the field from the Orders object
                    fields[j].setAccessible(true); // Allow access to private fields
                    Object value = fields[j].get(order);

                    // Set the cell value with the corresponding value from the Orders object
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(""); // Handle null values as needed
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        // Save the workbook to a file
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportToExcelBuy(Context context, List<Buy> buyList, String fileName) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        Buy orders = new Buy();
        // Get the class of the object
        Class<?> objectClass = orders.getClass();

        // Get all fields of the class
        Field[] fields = objectClass.getDeclaredFields();

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Iterate through the fields and print their names
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fieldName);
        }


        // Create data rows
        for (int i = 0; i < buyList.size(); i++) {
            Buy reportTrxItem = buyList.get(i);
            Row dataRow = sheet.createRow(i + 1); // Adjust index to start from row 1

            for (int j = 0; j < fields.length; j++) {
                Cell cell = dataRow.createCell(j);

                try {
                    // Use reflection to get the value of the field from the Orders object
                    fields[j].setAccessible(true); // Allow access to private fields
                    Object value = fields[j].get(reportTrxItem);

                    // Set the cell value with the corresponding value from the Orders object
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(""); // Handle null values as needed
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        // Save the workbook to a file
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportToExcelItemTrx(Context context, List<ReportTrxItem> reportTrxItemList, String fileName) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        ReportTrxItem orders = new ReportTrxItem();
        // Get the class of the object
        Class<?> objectClass = orders.getClass();

        // Get all fields of the class
        Field[] fields = objectClass.getDeclaredFields();

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Iterate through the fields and print their names
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fieldName);
        }


        // Create data rows
        for (int i = 0; i < reportTrxItemList.size(); i++) {
            ReportTrxItem reportTrxItem = reportTrxItemList.get(i);
            Row dataRow = sheet.createRow(i + 1); // Adjust index to start from row 1

            for (int j = 0; j < fields.length; j++) {
                Cell cell = dataRow.createCell(j);

                try {
                    // Use reflection to get the value of the field from the Orders object
                    fields[j].setAccessible(true); // Allow access to private fields
                    Object value = fields[j].get(reportTrxItem);

                    // Set the cell value with the corresponding value from the Orders object
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(""); // Handle null values as needed
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        // Save the workbook to a file
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportToExcelItemStock(Context context, List<ReportTrxItemStock> reportTrxItemList, String fileName) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        ReportTrxItemStock orders = new ReportTrxItemStock();
        // Get the class of the object
        Class<?> objectClass = orders.getClass();

        // Get all fields of the class
        Field[] fields = objectClass.getDeclaredFields();

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Iterate through the fields and print their names
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fieldName);
        }


        // Create data rows
        for (int i = 0; i < reportTrxItemList.size(); i++) {
            ReportTrxItemStock reportTrxItem = reportTrxItemList.get(i);
            Row dataRow = sheet.createRow(i + 1); // Adjust index to start from row 1

            for (int j = 0; j < fields.length; j++) {
                Cell cell = dataRow.createCell(j);

                try {
                    // Use reflection to get the value of the field from the Orders object
                    fields[j].setAccessible(true); // Allow access to private fields
                    Object value = fields[j].get(reportTrxItem);

                    // Set the cell value with the corresponding value from the Orders object
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(""); // Handle null values as needed
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        // Save the workbook to a file
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

    public static void exportToExcelCategories(Context context, List<Categories> categoriesList, String fileName) {
        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");

        Categories orders = new Categories();
        // Get the class of the object
        Class<?> objectClass = orders.getClass();

        // Get all fields of the class
        Field[] fields = objectClass.getDeclaredFields();

        // Create a header row
        Row headerRow = sheet.createRow(0);

        // Iterate through the fields and print their names
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fieldName);
        }


        // Create data rows
        for (int i = 0; i < categoriesList.size(); i++) {
            Categories reportTrxItem = categoriesList.get(i);
            Row dataRow = sheet.createRow(i + 1); // Adjust index to start from row 1

            for (int j = 0; j < fields.length; j++) {
                Cell cell = dataRow.createCell(j);

                try {
                    // Use reflection to get the value of the field from the Orders object
                    fields[j].setAccessible(true); // Allow access to private fields
                    Object value = fields[j].get(reportTrxItem);

                    // Set the cell value with the corresponding value from the Orders object
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(""); // Handle null values as needed
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        // Save the workbook to a file
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Excel file exported successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting Excel file", Toast.LENGTH_SHORT).show();
        }
    }

}
