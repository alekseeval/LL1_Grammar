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

        // Использовать стек? Тогда как строить вывод (т.е. на каждой иттерации добавлять переход)?
        // Или юзать StringBuffer?
        // Делать иттерацию внутри цикла while(!word.equals("e")) , где 'e' - конец строки? (для стека просто поднимать верхний элемент)

        // Стек для хранения вывода
        Stack<Character> parse = new Stack<>();
        // Стек для хранения строки
        Stack<Character> str = new Stack<>();

        // Заполнение стека со строкой
        for (int i = word.length() - 1; i >= 0; i--) {
            str.add(word.charAt(i));
        }
        parse.add('S'); // Начальный символ - S

        // Строка вывода
        String  current = "S";
        StringBuilder output = new StringBuilder("S");

        int count = 0; // Позиция в строке, на которой мы стоим (текущий символ)
        while (!str.empty() && !parse.empty() ){
            current = current.substring(1); // Убрать из current первый символ


            // Получаем из стеков значения сверху
            char nonTerm = parse.pop();
            char term = str.pop();

            // Если в стеке нетерминалов лежит терминал, то убираем его из двух стеков
            if (nonTerm < 'A' || nonTerm > 'Z'){
                count++;
                continue;
            }
            output.append(" -> ");

            str.add(term); // Возвращаем терминал обратно в стек
            // Получаем номер правила из таблица разбора
            int ruleNumber = this.table[this.nonTerminals.get(nonTerm)][this.terminals.get(term)];

            // Получаем правую часть правила
            String right = this.rules[ruleNumber][1];

            if (right.equals("E")){
                output.append(word.substring(0, count))
                        .append(current);
                continue;
            }

            // Заменить current строку
            current = right + current;
            // Добавить current в output
            output.append(word.substring(0, count))
                    .append(current);

            // Кладем правую часть правила в стек
            for (int i = right.length()-1; i >= 0 ; i--) {
                parse.add(right.charAt(i));
            }

        }

        return output.toString();
    }

}
