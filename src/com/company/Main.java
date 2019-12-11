package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        // Считывание из файла соответствующих данных
        HashMap<Character, Integer>[] t = readTerminalsFromFile("terms.txt");
        String[][] rules = readRulesFromFile("rules.txt");
        int[][] table = readTableFromFile("table.txt");

        // Создание экземпляра класса, который будет строить вывод
        DownParser dp = new DownParser(t[0], t[1], rules, table);

        // Считывание слова из консоли
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your word >> ");
//        String word = in.next();
//        String word = "(n+x*n)*(n-n)e"
        String word = "(x<x)v(x>x)e";

        System.out.println("Построенный вывод:");
        System.out.println(dp.getDef(word));

    }

    // Считывание таблицы разбора
    private static int[][] readTableFromFile(String str) throws FileNotFoundException {

        Scanner in = new Scanner(new File(str));
        String[] size = in.nextLine().split(" ");
        int row = Integer.parseInt(size[0]);
        int col = Integer.parseInt(size[1]);
        int[][] res = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res[i][j] = in.nextInt();
            }
        }

        return res;
    }

    // Считывание терминалов [0] и нетерминалов [1]
    private static HashMap<Character, Integer>[] readTerminalsFromFile(String str) throws FileNotFoundException {

        Scanner in = new Scanner(new File(str));
        HashMap<Character, Integer>[] res = new HashMap[2];

        for (int i = 0; i < 2; i++) {
            res[i] = new HashMap<>();
            String[] line = in.nextLine().split(" ");
            for (int j = 0; j < line.length; j++) {
                res[i].put(line[j].charAt(0), j);
            }
        }

        return res;
    }

    // Считывание правил
    private static String[][] readRulesFromFile(String str) throws FileNotFoundException {

        Scanner in = new Scanner(new File(str));
        int size = Integer.parseInt(in.nextLine());
        String[][] res = new String[size][];

        for (int i = 0; in.hasNext(); i++) {
            res[i] = in.nextLine().split(" ", 2);
        }

        return res;
    }
}
