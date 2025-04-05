class ThreadUtil {
    static Thread createThreadInNewState() {
        //implement the method
        return new Thread();
    }
}

class Main {
    public static void main(String[] args) {
        Thread thread = ThreadUtil.createThreadInNewState();
        System.out.println(thread.getState());
    }
}