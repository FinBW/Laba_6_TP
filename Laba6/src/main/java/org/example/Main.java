package org.example;

import java.util.Arrays;

public class Main {
    static final int SIZE = 6_000_008;
    static final int HALF = SIZE / 2;
    static final int COUNT_THREAD = give_count_threads(SIZE);

    public static void main(String[] args) {
        // Базовое задание: с одним массивом:
        float[] first = new float[SIZE];
        Arrays.fill(first, 1.0f);
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            first[i] = (float) (first[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                    Math.cos(0.4f + i / 2));
        }
        System.out.println("Время создания массива первым методом: " + (System.currentTimeMillis() - time1));
        System.out.println("Первое число после первого метода : " + first[0]);
        System.out.println("Последнее число после первого метода : " + first[SIZE - 1]);
        System.out.println();
        // с разделёнными массивами:
        float[] second = new float[SIZE];
        Arrays.fill(second, 1.0f);

        float[] second_1 = new float[HALF];
        float[] second_2 = new float[HALF];

        long time2 = System.currentTimeMillis();
        //делим массив на два
        System.arraycopy(second, 0, second_1, 0, HALF);
        System.arraycopy(second, HALF, second_2, 0, HALF);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < HALF; i++) {
                second_1[i] = (float) (second_1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < HALF; i++) {
                second_2[i] = (float) (second_2[i] * Math.sin(0.2f + (i + HALF) / 5) * Math.cos(0.2f + (i + HALF) / 5) *
                        Math.cos(0.4f + (i + HALF) / 2));
            }
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //склеиваем массивы в один
        System.arraycopy(second_1, 0, second, 0, HALF);
        System.arraycopy(second_2, 0, second, HALF, HALF);

        System.out.println("Время создания массива вторым методом : " + (System.currentTimeMillis() - time2));

        System.out.println("Первое число после второго метода : " + second[0]);
        System.out.println("Последнее число после второго метода : " + second[SIZE - 1]);  // =1.0 если размер массива нечётный
        System.out.println("Совпадают ли массивы? : " + Arrays.equals(first, second));
        System.out.println();

        // Индивидуальное задание:
        float[] individ = new float[SIZE];
        Arrays.fill(individ, 1.0f);
        long timeIndivid = System.currentTimeMillis();
        Thread[] threads = new Thread[COUNT_THREAD];
        for (int i = 0; i < COUNT_THREAD; i++) {
            int index_start = i * (SIZE / COUNT_THREAD);
            int index_end = (i + 1) * (SIZE / COUNT_THREAD);
            Thread my_thread = new Thread(() -> {
                for (int j = index_start; j < index_end; j++) {
                    individ[j] = (float) (individ[j] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) *
                            Math.cos(0.4f + j / 2));
                }
            });
            threads[i] = my_thread;
            threads[i].start();
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Времени потрачено на индивидуальное задание : " + (System.currentTimeMillis() - timeIndivid));
        System.out.println("Количество потоков для индивидуального задания : " + COUNT_THREAD);
        System.out.println("Первое число после второго метода : " + individ[0]);
        System.out.println("Последнее число после индивидуального метода : " + individ[SIZE - 1]);
    }

    public static int give_count_threads(int array_size) {
        int result = 6;
        while (array_size % result != 0) {
            result++;
        }
        return result;
    }
}
