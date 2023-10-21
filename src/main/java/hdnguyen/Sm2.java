package hdnguyen;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Sm2 {



    public static Sm2Value calc(int quality, int repetitions, int previousInterval, float previousEaseFactor) {
        Sm2Value sm2Value = new Sm2Value();
        if (quality >= 3) { // good
            if (repetitions == 0) {
                sm2Value.setInterval(1);
            }
            else if (repetitions == 1) {
                sm2Value.setInterval(4);
            }
            else {
                sm2Value.setInterval((int) (previousInterval * previousEaseFactor));
            }
            sm2Value.setRepetitions(repetitions + 1);

            float eFactor = Math.min((float)(previousEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))),2.5f);
            String roundedNumber = String.format("%.3f", eFactor);
            sm2Value.setEFactor(Float.parseFloat(roundedNumber));
        }
        else { // bad
            sm2Value.setRepetitions(0);
            sm2Value.setInterval(1);
            sm2Value.setEFactor(2f);
        }

        if (sm2Value.getEFactor() < 1.3f) {
            sm2Value.setEFactor(1.3f);
        }
        return sm2Value;
    }

    public static void main(String[] args) {
        int [] cardResponses = {0, 3, 4,1 , 5, 5, 1, 4, 5, 5, 5, 4, 3, 4, 5};
            int repetitions = 0;
            int previousInterval = 1;
            float previousEFactor = 2f;

            for (int cardResponse : cardResponses)  {
                Sm2Value sm2Value = Sm2.calc(cardResponse,repetitions, previousInterval, previousEFactor);
                repetitions = sm2Value.getRepetitions();
                previousInterval = sm2Value.getInterval();
                previousEFactor = sm2Value.getEFactor();
                System.out.println(repetitions + " " + cardResponse + " " + previousEFactor+ " " + previousInterval);
            }
    }
}

@Getter
@Setter
class Sm2Value {
    private int repetitions;
    private float eFactor;
    private int interval;
}
