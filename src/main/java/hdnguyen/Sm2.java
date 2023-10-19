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
                sm2Value.setInterval(6);
            }
            else {
                sm2Value.setInterval((int) (previousInterval * previousEaseFactor));
            }
            sm2Value.setRepetitions(repetitions + 1);
            sm2Value.setEFactor((float)(previousEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))));
        }
        else { // bad
            sm2Value.setRepetitions(0);
            sm2Value.setInterval(1);
            sm2Value.setEFactor(previousEaseFactor);
        }

        if (sm2Value.getEFactor() < 1.3f) {
            sm2Value.setEFactor(1.3f);
        }
        return sm2Value;
    }

    public static void main(String[] args) {
        int [] cardResponses = {1, 1, 3, 2, 3, 4, 5};
        int repetitions = 0; // chưa học lần nào.
        int previousInterval = 1;
        float previousEaseFactor = 1f;

        for (int cardResponse : cardResponses)  {
            Sm2Value sm2Value = Sm2.calc(cardResponse,repetitions, previousInterval, previousEaseFactor);
            repetitions = sm2Value.getRepetitions();
            previousInterval = sm2Value.getInterval();
            previousEaseFactor = sm2Value.getEFactor();
            System.out.println(repetitions + " " + previousEaseFactor+ " " + previousInterval);
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
