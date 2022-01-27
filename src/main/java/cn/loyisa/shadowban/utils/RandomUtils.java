package cn.loyisa.shadowban.utils;

import java.util.Random;

public class RandomUtils {

    private static final Random random = new Random();

    public static int nextInt(int min, int max) {
        return (int) nextDouble(min, max);
    }

    public static float nextFloat(float min, float max) {
        return (float) nextDouble(min, max);
    }

    public static long nextLong(long min, long max) {
        return (long) nextDouble(min, max);
    }

    public static double nextDouble(double min, double max) {
        return max + (min - max) * random.nextDouble();
    }

    public static boolean nextBoolean() {
        return random.nextBoolean();
    }

    /**
     * 炒强无敌概率算法
     *
     * @param probability 概率值
     * @return 如果成了就返回true
     */
    public static boolean nextProbability(int probability) {
        return random.nextInt(100) <= probability;
    }

}
