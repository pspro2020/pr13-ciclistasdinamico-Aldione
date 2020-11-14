import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CiclistaImpaciente implements Runnable{

    private final String name;
    private final Phaser phaser;

    public CiclistaImpaciente(String name, Phaser phaser) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        phaser.register();
        try {
            goToGasStation();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while is going to the gas station.\n", name);
            return;
        }
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive());
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while waiting for friends in the gas station.\n", name);
            return;
        }
        phaser.arriveAndDeregister();
        try {
            firstStage();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going to the first stage.\n", name);
            return;
        }
        try {
            secondStage();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going to the second stage.\n", name);
            return;
        }
        try {
            goHome();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted while going back home\n", name);
        }
    }

    private void goHome() throws InterruptedException {
        System.out.printf("%s - %s is going home.\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s - %s arrieved home.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void secondStage() throws InterruptedException {
        System.out.printf("%s - %s start the second stage (go back to gas station).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10)+5);
        System.out.printf("%s - %s finished the second stage (arrieved gas station).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void firstStage() throws InterruptedException {
        System.out.printf("%s - %s start the first stage (going to the venta).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10)+5);
        System.out.printf("%s - %s finished the first stage (arrieved to the venta).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void goToGasStation() throws InterruptedException {
        System.out.printf("%s - %s is going to the Gas Station.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s - %s arrieved to the Gas Station.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
    }
}
