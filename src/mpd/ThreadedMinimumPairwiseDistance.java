package mpd;

import java.io.IOException;
import java.lang.Thread;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {

    private int[] values;
    private long globalResult = Integer.MAX_VALUE;

    @Override
    public long minimumPairwiseDistance(int[] values) {
        this.values = values;

        Thread threadLL = new Thread(new LowerLeft());
        threadLL.start();
        Thread threadTR = new Thread(new TopRight());
        threadTR.start();
        Thread threadBR = new Thread(new BottomRight());
        threadBR.start();
        Thread threadM = new Thread(new Middle());
        threadM.start();

        try {
            threadLL.join();
            threadBR.join();
            threadTR.join();
            threadM.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return globalResult;

    }

    public synchronized void setGlobalResult(long min) {

        globalResult = min;
    }

    class LowerLeft implements Runnable {

        public void run() {

            for(int i = 0; i < values.length/2; ++i) {
                for (int j = 0; j < i; ++j) {
                    // Gives us all the pairs (i, j) where 0 ≤ j < i < N/2

                    long diff = Math.abs((long) values[i] - values[j]);
                    if (diff < globalResult) {
                        setGlobalResult(diff);
                    }
                }
            }
        }

    }

    class TopRight implements Runnable {

        public void run() {

            for(int i = values.length/2; i < values.length; ++i) {
                for (int j = values.length/2; j < i; ++j) {
                    // Gives us all the pairs (i, j) where N/2 ≤ j < i < N

                    long diff = Math.abs((long) values[i] - values[j]);
                    if (diff < globalResult) {
                        setGlobalResult(diff);
                    }
                }
            }

        }

    }

    class BottomRight implements Runnable {

        public void run() {

            for(int i = values.length/2; i < values.length; ++i) {
                for (int j = 0; j < i-(values.length/2); ++j) {
                    // Gives us all the pairs (i, j) where N/2 ≤ j + N/2 < i < N

                    long diff = Math.abs((long) values[i] - values[j]);
                    if (diff < globalResult) {
                        setGlobalResult(diff);
                    }
                }
            }

        }

    }

    class Middle implements Runnable {

        public void run() {

            for(int j = 0; j < values.length-values.length/2; ++j) {
                for (int i = values.length/2; i <= j+values.length/2; ++i) {
                    // Gives us all the pairs (i, j) where N/2 ≤ i ≤ j + N/2 < N

                    long diff = Math.abs((long) values[j] - values[i]);
                    if (diff < globalResult) {
                        setGlobalResult(diff);
                    }
                }
            }

        }

    }


}


