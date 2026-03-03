public class Utilites {
    public static void printWithTimer(String line, int Timer) {
        char letters[] = line.toCharArray();
        for (char letter : letters) {
            System.out.print(letter);
            try {
                Thread.sleep(Timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
