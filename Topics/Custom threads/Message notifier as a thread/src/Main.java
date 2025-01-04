class MessageNotifier extends Thread {

    // write fields to store variables here
    String msg;
    int repeats;

    public MessageNotifier(String msg, int repeats) {
        this.msg = msg;
        this.repeats = repeats;
    }

    @Override
    public void run() {
        // implement the method to print the message stored in a field
        int i = 0;
        while (i < this.repeats) {
            System.out.println(this.msg);
            i++;
        }
    }
}
