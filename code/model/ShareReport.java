package model;

import viewcontrol.PrintableText;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ShareReport {
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ShareReport() {
    }

    public void sendEmail(String recipient) {
        try {
            Socket s = new Socket("osfmail.rit.edu", 25);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(s.getInputStream(), "8859_1"));
            BufferedWriter out =
                    new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream(), "8859_1"));

//            String boundary = "DataSeparatorString";

            // here you are supposed to send your username
            sendln(in, out, "HELO world");
            sendln(in, out, "MAIL FROM: <abc1234@rit.edu>");
            sendln(in, out, "RCPT TO: <" + recipient + ">");
            sendln(in, out, "DATA");
            sendln(out, "Subject: Bowling Score Report ");
            sendln(out, "From: <Lucky Strikes Bowling Club>");

            sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
            sendln(out, content + "\n\n");
            sendln(out, "\r\n");

            sendln(in, out, ".");
            sendln(in, out, "QUIT");
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPrintout() {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintableText printobj = new PrintableText(content);
        job.setPrintable(printobj);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.out.println(e);
            }
        }
    }

    public void sendln(BufferedReader in, BufferedWriter out, String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            s = in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendln(BufferedWriter out, String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}