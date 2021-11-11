package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        VareData[] varedata = new VareData[5];

        File f = new File("bestilling.txt");
        Scanner scanner = new Scanner(f); // brug stien/file-objektet i stedet for System.in

        for (int i = 0; i < varedata.length; i++) {
            varedata[i] = new VareData();

            varedata[i].setAntal(Integer.valueOf(scanner.nextInt()));
            varedata[i].setVarenavn(scanner.next());
            varedata[i].setStkPris(Double.valueOf(scanner.nextDouble()));
            scanner.nextLine();
        }

        //printVareData(varedata);

        //samletPrisPrVare(varedata);

        //System.out.println("Saldo med rabat: " + saldoMedRabat(varedata));

        //System.out.println("Saldo uden rabat: " + saldoUdenRabat(varedata));

        //faktura(varedata);

        writeToSerFile(varedata, "varer.ser");

        varedata = printSerFileToVareData("varer.ser");

        printVareData(varedata);
    }

    public static void printVareData(VareData[] varedata) {
        for (int i = 0; i < varedata.length; i++) {
            System.out.println("Antal:\t" + varedata[i].getAntal() + "\tVarenavn:\t" + varedata[i].getVarenavn() + "\tStk. Pris:\t" + varedata[i].getStkPris());
        }
    }

/*    public static void writeToSerFile(VareData[] varedata, String serFilename) throws IOException {
        DataOutputStream output2 = new DataOutputStream(new FileOutputStream(serFilename));
        for (int j = 0; j < varedata.length; j++) {
            output2.write(varedata[j].getAntal());
            output2.writeUTF(varedata[j].getVarenavn());
            output2.writeDouble(varedata[j].getStkPris());
        }
        output2.close();
    }*/

    public static void writeToSerFile(VareData[] varedata, String serFilename) throws IOException {
        ObjectOutputStream output2 = new ObjectOutputStream(new FileOutputStream(serFilename));
        for (int j = 0; j < varedata.length; j++) {
            output2.writeObject(varedata[j]);
        }
        // Sæt EOF marker:
        output2.writeObject(new EndOfFile());
        output2.close();
    }

    public static VareData[] printSerFileToVareData(String serFilename) throws IOException {

        ObjectInputStream input = new ObjectInputStream(new FileInputStream(serFilename));
        VareData[] varedata = new VareData[5];

        Object obj = null;
        int i = 0;
        try {
            while ((obj = input.readObject()) instanceof EndOfFile == false) {
/*                System.out.println(((VareData)obj).getVarenavn());
                System.out.println(((VareData)obj).getAntal());
                System.out.println(((VareData)obj).getStkPris());*/
                varedata[i] = (VareData) obj;
                i++;
            }
            //varedata = (VareData[]) input.readObject();
            input.close();
            return varedata;
        } catch (Exception e) {
            System.out.println("Fejl:" + e.getMessage());
        }
        return null;
    }

    /*public static VareData[] printSerFileToVareData(String serFilename) throws IOException {

        ObjectInputStream input = new ObjectInputStream(new FileInputStream(serFilename));
        VareData[] varedata;
        try {
            varedata = (VareData[]) input.readObject();
            input.close();
            return varedata;
        } catch (Exception e) {
            System.out.println("Fejl:" + e.getMessage());
        }
        return null;
    }*/



/*    public static void printSerFileToVareData(String serFilename, VareData[] varedata) throws IOException {

        DataInputStream input = new DataInputStream(new FileInputStream(serFilename));
        try {
            for (int i = 0; i < varedata.length; i++) {
                varedata[i] = new VareData();
                varedata[i].setAntal(input.read());
                varedata[i].setVarenavn(input.readUTF());
                varedata[i].setStkPris(input.readDouble());
                System.out.println(varedata[i].getAntal() + "\t" + varedata[i].getVarenavn() + "\t" + varedata[i].getStkPris() + "\t");
            }
            input.close();
        } catch (Exception e) {
            System.out.println("Fejl:" + e.getMessage());
        }
    }*/

    public static void samletPrisPrVare(VareData[] varedata) {
        Double result = 0.0;

        for (int i = 0; i < varedata.length; i++) {
            result = varedata[i].getAntal() * varedata[i].getStkPris();
            if (varedata[i].getAntal() > 10) {
                System.out.println("Samlet pris for: " + varedata[i].getVarenavn() + ":\t" + result + ". Samlet pris (inkl rabat) for: " + varedata[i].getVarenavn() + ":\t" + result * 0.85);
            } else {
                System.out.println("Samlet pris for: " + varedata[i].getVarenavn() + ":\t" + result + ". Samlet pris (inkl rabat) for: " + varedata[i].getVarenavn() + ":\t" + result);
            }
        }
    }

    public static Double saldoMedRabat(VareData[] varedata) {
        Double result = 0.0;

        for (int i = 0; i < varedata.length; i++) {
            result = result + varedata[i].getAntal() * varedata[i].getStkPris();
            if (varedata[i].getAntal() > 10) {
                result = result * 0.85;
            }
        }

        return result;
    }

    public static Double saldoUdenRabat(VareData[] varedata) {
        Double result = 0.0;

        for (int i = 0; i < varedata.length; i++) {
            result = result + varedata[i].getAntal() * varedata[i].getStkPris();
        }
        return result;
    }

    public static void faktura(VareData[] varedata) {
        samletPrisPrVare(varedata);
        System.out.println(saldoUdenRabat(varedata));
        System.out.println(saldoMedRabat(varedata));
    }

}
class EndOfFile implements Serializable {

}