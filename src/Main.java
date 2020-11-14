import java.util.concurrent.TimeUnit;

public class Main {

    private static final int NUMBER_OF_FRIENDS = 10;

    public static void main(String[] args) throws InterruptedException{
        CyclicPhaser phaser = new CyclicPhaser();
        int i;

        for (i = 0; i < NUMBER_OF_FRIENDS; i++) {
            new Thread(new Ciclista("Cyclist " + i, phaser), "Cyclist " + i).start();
        }
        new Thread(new CiclistaImpaciente("Cyclist " + i, phaser), "Cyclist " + i).start();
        i++;
        TimeUnit.SECONDS.sleep(12);
        new Thread(new CiclistaTardon("Cyclist " + i, phaser), "Cyclist " + i).start();
        i++;
        TimeUnit.SECONDS.sleep(29);
        new Thread(new CiclistaTardon("Cyclist " + i, phaser), "Cyclist " + i).start();
    }

}
