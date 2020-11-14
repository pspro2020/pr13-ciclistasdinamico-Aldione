import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.Phaser;

public class CyclicPhaser extends Phaser {

    public static final int ARRIVE_GAS_STATION = 0;
    public static final int FINISH_FIRST_STAGE = 1;
    public static final int FINISH_SECOND_STAGE = 2;

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case ARRIVE_GAS_STATION -> System.out.printf("%s -> All %d friends arrived to the gas station (executed in %s)\n",
                    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), registeredParties,
                    Thread.currentThread().getName());
            case FINISH_FIRST_STAGE -> System.out.printf("%s -> All %d friends finished their first stage (executed in %s)\n",
                    DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), registeredParties,
                    Thread.currentThread().getName());
            case FINISH_SECOND_STAGE -> {
                System.out.printf("%s -> All %d friends finished their second stage (executed in %s)\n",
                        DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), registeredParties,
                        Thread.currentThread().getName());
                return true;
            }
        }
        return super.onAdvance(phase, registeredParties);
    }

}
