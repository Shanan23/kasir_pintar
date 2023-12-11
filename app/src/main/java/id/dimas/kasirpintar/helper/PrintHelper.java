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
        /*new AsyncBluetoothEscPosPrint(
                context,
                new AsyncEscPosPrint.OnPrintFinished() {
                    @Override
                    public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                        Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                    }

                    @Override
                    public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                        Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                    }
                }
        )
                .execute(this.getAsyncEscPosPrinter(context, selectedDevice, orders, ordersDetail));*/
    }

    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(Context context, DeviceConnection printerConnection, Orders orders, List<OrdersDetail> ordersDetailList) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        return printer.addTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.ic_logo_black, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[L]\n" +
                        "[C]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        "[C]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99€\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99€\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98€\n" +
                        "[R]TAX :[R]4.23€\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "\n"
        );
    }

}
