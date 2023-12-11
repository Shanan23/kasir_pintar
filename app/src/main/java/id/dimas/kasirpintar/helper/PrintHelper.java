package id.dimas.kasirpintar.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnections;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.dimas.kasirpintar.R;
import id.dimas.kasirpintar.model.Orders;
import id.dimas.kasirpintar.model.OrdersDetail;

public class PrintHelper {

    public BluetoothConnection selectedDevice;

    public void browseBluetoothDevice(Context context) {
        final BluetoothConnection[] bluetoothDevicesList = (new BluetoothConnections()).getList();

        if (bluetoothDevicesList != null) {
            final String[] items = new String[bluetoothDevicesList.length + 1];
            items[0] = "Default printer";
            int i = 0;
            for (BluetoothConnection device : bluetoothDevicesList) {
                items[++i] = device.getDevice().getName();
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Bluetooth printer selection");
            alertDialog.setItems(
                    items,
                    (dialogInterface, i1) -> {
                        int index = i1 - 1;
                        if (index == -1) {
                            selectedDevice = null;
                        } else {
                            selectedDevice = bluetoothDevicesList[index];
                        }
//                            Button button = (Button) findViewById(R.id.button_bluetooth_browse);
//                            button.setText(items[i1]);
                    }
            );

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

    }

    public void printBluetooth(Context context, Orders orders, List<OrdersDetail> ordersDetailList) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        EscPosPrinter printer = null;
        try {
            printer = new EscPosPrinter(selectedDevice, 203, 48f, 32);
            StringBuilder printText = new StringBuilder("[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.ic_logo_print, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                    "[L]\n" +
                    "[C]<font size='wide'>Nama Toko</font>\n" +
                    "[C]<font size='wide'>Alamat Toko</font>\n" +
                    "[L]\n" +
                    "[C]================================\n" +
                    "[L]Id Transaksi : #" + orders.getId() + "\n" +
                    "[L]Tanggal :" + orders.getOrderDate() + "\n" +
                    "[L]Pelanggan :" + orders.getCustomerId() + "\n" +
                    "[C]================================\n");

            for (OrdersDetail ordersDetail : ordersDetailList) {
                printText.append("[L]").append(ordersDetail.getProducts().getName()).append("\n");
                printText.append("[L]")
                        .append(ordersDetail.getQty())
                        .append("*")
                        .append(String.format("Rp %s", decimalFormat.format(Double.parseDouble(ordersDetail.getProducts().getSellPrice()))).replace(",", "."));
                printText.append("[R]")
                        .append(String.format("Rp %s", decimalFormat.format(ordersDetail.getTotalDetails())).replace(",", "."));
                printText.append("\n");
            }
            printText.append("[C]================================\n");

            printText.append("[L]Subtotal");
            printText.append("[R]")
                    .append(String.format("Rp %s", decimalFormat.format(orders.getAmount())).replace(",", "."));
            printText.append("\n");
            printText.append("[C]================================\n");
            printText.append("[R]Total Harga : ")
                    .append(String.format("Rp %s", decimalFormat.format(orders.getAmount())).replace(",", "."));
            printText.append("\n");
            printText.append("[R]Bayar : ")
                    .append(String.format("Rp %s", decimalFormat.format(orders.getPayAmount())).replace(",", "."));
            printText.append("\n");

            if (orders.getOrderStatus().equalsIgnoreCase("COMPLETED")) {
                printText.append("[R]Kembalian : ")
                        .append(String.format("Rp %s", decimalFormat.format(orders.getDifferent())).replace(",", "."));
                printText.append("\n\n");
            }

            printText.append("[C]TERIMAKASIH TELAH BERBELANJA\n\n");
            printer.printFormattedText(printText.toString());
        } catch (EscPosConnectionException | EscPosParserException | EscPosEncodingException |
                 EscPosBarcodeException e) {
            e.printStackTrace();
        }

    }
}
