package com.company;

import java.util.HashMap;
import java.util.Stack;

public class DownParser {

    // Отображение терминалов в цифры для удобства использования таблицы
    HashMap<Character, Integer> terminals;
    // Отображение НЕ терминалов в цифры
    HashMap<Character, Integer> nonTerminals;
    // Массив правил вида [["S", "Pe"], ["P", "QR"] ...]
    // т.е. он всегда двумерный
    String[][] rules;
    // Таблица разбора, внутри которой номера применяемых правил
    int[][] table;

    public DownParser(HashMap<Character, Integer> terminals, HashMap<Character, Integer> nonTerminals, String[][] rules, int[][] table) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.rules = rules;
        this.table = table;
    }

    String getDef(String word){

        // Стек для хранения вывода
        Stack<Character> parse = new Stack<>();
        // Стек для хранения строки
        Stack<Character> str = new Stack<>();

        // Заполнение стека со строкой
        for (int i = word.length() - 1; i >= 0; i--) {
            // Проверка, принадлежит ли терминал грамматике
            if (this.terminals.get(word.charAt(i)) == null){
                System.out.printf("Неожиданный символ: \"%c\" На позиции: %d\n", word.charAt(i), i);
                System.exit(0);
            }
            str.add(word.charAt(i));
        }
        parse.add('S'); // Начальный символ - S

        // Строка вывода
        String  current = "S";
        StringBuilder output = new StringBuilder("S");

        int count = 0; // Позиция в строке, на которой мы стоим (текущий символ)
        while (!str.empty() || !parse.empty()){

            // Если стек пустой, а строка не кончилась
            if (parse.empty()){
                System.out.print("Введены неожиданные символы: ");
                StringBuilder buf = new StringBuilder();
                while (!str.empty()){
                    buf.append(str.pop());
                }
                System.out.println(buf.toString());
                System.exit(0);
            }

            // Если строка кончилась, а в стеке остались символы
            if (str.empty()){
                System.out.println("Ожидались еще символы");
                System.exit(0);
            }

            current = current.substring(1); // Убрать из current первый символ

            // Получаем из стеков значения сверху
            char nonTerm = parse.pop();
            char term = str.pop();

            // Если в стеке нетерминалов лежит терминал, то убираем его из двух стеков
            if (nonTerm < 'A' || nonTerm > 'Z'){
                // Терминалы должны совпадать
                if (nonTerm != term){
                    System.out.printf("Встречен неопознанный символ: \"%c\", на позиции: %d\n", term, count);
                    System.exit(0);
                }
                count++;
                continue;
            }
            output.append(" -> ");

            str.add(term); // Возвращаем терминал обратно в стек
            // Получаем номер правила из таблица разбора
            int ruleNumber = this.table[this.nonTerminals.get(nonTerm)][this.terminals.get(term)];

            // Проверка существовния правила
            if (ruleNumber == -1){
                System.out.printf("Ожидался другой символ на позиции: %d\n", count);
                System.exit(0);
            }

            // Получаем правую часть правила
            String right = this.rules[ruleNumber][1];

            if (right.equals("E")){
                output.append(word, 0, count)
                        .append(current);
                continue;
            }

            // Заменить current строку
            current = right + current;
            // Добавить current в output
            output.append(word, 0, count)
                    .append(current);

            // Кладем правую часть правила в стек
            for (int i = right.length()-1; i >= 0 ; i--) {
                parse.add(right.charAt(i));
            }

        }

        return output.toString();
    }

}
