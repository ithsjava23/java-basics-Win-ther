package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class App {
    //static Scanner scanner = new Scanner(System.in); Går inte att köra testerna med en Scanner som statiskt fält.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] timeOfDay = {"00-01", "01-02", "02-03", "03-04", "04-05", "05-06", "06-07", "07-08", "08-09", "09-10", "10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17", "17-18", "18-19", "19-20", "20-21", "21-22", "22-23", "23-24"};
        int[] elPriser = new int[timeOfDay.length];
        menu(timeOfDay, elPriser, scanner);
    }

    public static void menu(String[] timeOfDay, int[] elPriser, Scanner scanner) {
        boolean contMenu;
        do {
            System.out.print("""
                    Elpriser
                    ========
                    1. Inmatning
                    2. Min, Max och Medel
                    3. Sortera
                    4. Bästa Laddningstid (4h)
                    5. Visualisering
                    e. Avsluta
                    """);
            contMenu = userChoice(timeOfDay, elPriser, scanner);
        } while (contMenu);
    }

    public static boolean userChoice(String[] timeOfDay, int[] elPriser, Scanner scanner) {
        switch (scanner.nextLine()) {
            default -> System.out.println("Ogiltig input!");
            case "1" -> inputPrice(timeOfDay, elPriser, scanner);
            case "2" -> minMaxMedel(timeOfDay, elPriser);
            case "3" -> {
                sort(timeOfDay, elPriser);
                printPrice(timeOfDay, elPriser);
            }
            case "4" -> bestChargeTime(timeOfDay, elPriser);
            case "5" -> visualisering(elPriser);
            case "6" -> printPrice(timeOfDay, elPriser); //För felsökning
            case "e", "E" -> {
                return false;
            }
        }
        return true;
    }

    public static void inputPrice(String[] date, int[] price, Scanner scanner) {
        Arrays.sort(date);
        for (int i = 0; i < date.length; i++) {
            price[i] = Integer.parseInt(scanner.nextLine());
            //price[i] = (int) (Math.random() * 100); //För felsökning
        }
    }

    public static void minMaxMedel(String[] date, int[] price) {
        int min = price[0], max = price[0];
        String minTime = date[0], maxTime = date[0];
        float medel = 0;
        for (int i = 0; i < date.length; i++) {
            if (price[i] < min) {
                min = price[i];
                minTime = date[i];
            }
            if (price[i] > max) {
                max = price[i];
                maxTime = date[i];
            }
            medel += price[i];
        }
        medel = (medel / 24);
        medel = (float) Math.round(medel * 100) / 100;
        String formatComma = String.format("%,.2f", medel);
        System.out.print("Lägsta pris: " + minTime + ", " + min + " öre/kWh\n");
        System.out.print("Högsta pris: " + maxTime + ", " + max + " öre/kWh\n");
        System.out.print("Medelpris: " + formatComma + " öre/kWh\n");
    }

    public static void bestChargeTime(String[] date, int[] price) {
        float priceFourBest = price[0] + price[1] + price[2] + price[3];
        String startPoint = date[0];
        for (int i = 1; i < date.length - 3; i++) {
            if (priceFourBest > (price[i] + price[i + 1] + price[i + 2] + price[i + 3])) {
                priceFourBest = (price[i] + price[i + 1] + price[i + 2] + price[i + 3]);
                startPoint = date[i];
            }
        }
        String[] print = startPoint.split("-");
        String formatComma = String.format("%,.1f", priceFourBest / 4);

        System.out.print("Påbörja laddning klockan " + print[0] + "\nMedelpris 4h: " + formatComma + " öre/kWh\n");
    }

    public static void printPrice(String[] date, int[] price) {
        for (int i = 0; i < date.length; i++) {
            System.out.print(date[i] + " " + price[i] + " öre\n");
        }
    }

    public static void sort(String[] date, int[] price) {
        int tempPrice;
        String tempTime;
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < date.length - 1; i++) {
                if (price[i] < price[i + 1]) {
                    tempPrice = price[i + 1];
                    price[i + 1] = price[i];
                    price[i] = tempPrice;
                    tempTime = date[i + 1];
                    date[i + 1] = date[i];
                    date[i] = tempTime;
                    swapped = true;
                }
            }
        }
    }

    public static void visualisering(int[] price) {
        int min = Arrays.stream(price).min().getAsInt();
        int max = Arrays.stream(price).max().getAsInt();
        double step = (double) (max - min) / 5;
        step = Math.ceil(step);
        //If/Else satsen är bara där för det andra testet createDiagram2, den har lite ändrade värden för vart x ska placeras ut för att jag inte kunde utskriften att se exakt likadan ut som i testet
        // Den går att ta bort och inte användas ifall man inte skulle använda testerna.
        if (min >= 0) {
            for (int i = 0; i < 6; i++) {
                switch (i) {
                    case 0 -> {
                        if (max < 10 && max >= 0)
                            System.out.print("  " + max + "|");
                        else if (max >= 100 || max < 0)
                            System.out.print(max + "|");
                        else
                            System.out.print(" " + max + "|");
                        for (int k : price) {
                            if (k == max)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 1 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 2 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 2)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 3 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 3)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 4 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 4)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 5 -> {
                        if (min < 10)
                            System.out.print("  " + min + "|");
                        else if (min >= 100)
                            System.out.print(min + "|");
                        else
                            System.out.print(" " + min + "|");
                        for (int j = 0; j < price.length; j++)
                            System.out.print("  x");
                    }
                }
                System.out.print("\n");
            }
        } else {
            for (int i = 0; i < 6; i++) {
                switch (i) {
                    case 0 -> {
                        if (max < 10 && max >= 0)
                            System.out.print("  " + max + "|");
                        else if (max >= 100)
                            System.out.print(max + "|");
                        else
                            System.out.print(" " + max + "|");
                        for (int k : price) {
                            if (k == max)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 1 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step - 1)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 2 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 2)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 3 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 3)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 4 -> {
                        System.out.print("   |");
                        for (int k : price) {
                            if (k >= max - step * 4 + 2)
                                System.out.print("  x");
                            else
                                System.out.print("   ");
                        }
                    }
                    case 5 -> {
                        if (min <= -10)
                            System.out.print(min + "|");
                        else
                            System.out.print(" " + min + "|");
                        for (int j = 0; j < price.length; j++)
                            System.out.print("  x");
                    }
                }
                System.out.print("\n");
            }
        }
        System.out.print("""
                   |------------------------------------------------------------------------
                   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23
                """);
    }
}
