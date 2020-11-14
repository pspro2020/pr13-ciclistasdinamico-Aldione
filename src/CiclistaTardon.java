import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CiclistaTardon implements Runnable{

    private final String name;
    private final Phaser phaser;

    public CiclistaTardon(String name, Phaser phaser) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(phaser);
        this.name = name;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        if(!phaser.isTerminated()){
            int joinPhase = phaser.register();
            System.out.printf("%s - %s has joined friends in phase %d\n",
                    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), name, joinPhase+1);
            try {
                goToGasStation();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while is going to the gas station.\n", name);
                return;
            }
            if(joinPhase <= CyclicPhaser.ARRIVE_GAS_STATION){
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s has been interrupted while waiting for friends in the gas station.\n", name);
                    return;
                }
            }
            try {
                firstStage();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going to the first stage.\n", name);
                return;
            }
            if(joinPhase <= CyclicPhaser.FINISH_FIRST_STAGE){
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s has been interrupted while waiting for friends to finish their first stage.\n", name);
                    return;
                }
            }
            try {
                secondStage();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going to the second stage.\n", name);
                return;
            }
            if (joinPhase <= CyclicPhaser.FINISH_SECOND_STAGE){
                try {
                    phaser.awaitAdvanceInterruptibly(phaser.arrive());
                } catch (InterruptedException e) {
                    System.out.printf("%s has been interrupted while waiting for friends to finish their second stage.\n", name);
                    return;
                }
            }
            try {
                goHome();
            } catch (InterruptedException e) {
                System.out.printf("%s has been interrupted while going back home\n", name);
            }
        }else{
            System.out.printf("%s - %s called his friends too late\n",
                    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), name);
        }

    }

    private void goHome() throws InterruptedException {
        System.out.printf("%s - %s is going home.\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s - %s arrieved home.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void secondStage() throws InterruptedException {
        System.out.printf("%s - %s start the second stage (go back to the gas station).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10)+5);
        System.out.printf("%s - %s finished the second stage (arrieved to the gas station).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void firstStage() throws InterruptedException {
        System.out.printf("%s - %s start the first stage (going to the venta).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10)+5);
        System.out.printf("%s - %s finished the first stage (arrieved to the venta).\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()) ,Thread.currentThread().getName());
    }

    private void goToGasStation() throws InterruptedException {
        System.out.printf("%s - %s is going to the Gas Station.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3)+1);
        System.out.printf("%s - %s arrieved the gas station.\n",DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName());
    }

    @SuppressWarnings("unused")
    private void awaitPhase(Phaser phaser, int currentPhase, int expectedPhase) {
        while (currentPhase < expectedPhase && !phaser.isTerminated()) {
            System.out.printf("%s - %s is waiting phase #%d to finish\n",
                    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), name, currentPhase);
            currentPhase = phaser.arriveAndAwaitAdvance();
        }
    }
}
