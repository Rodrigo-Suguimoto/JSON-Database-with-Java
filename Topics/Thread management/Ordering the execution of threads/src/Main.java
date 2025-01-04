class Invoker {

    public static void invokeMethods(Thread t1, Thread t2, Thread t3) throws InterruptedException {
        for (Thread thread : new Thread[]{t3, t2, t1}) {
            thread.start();
            thread.join();
        }
    }
}