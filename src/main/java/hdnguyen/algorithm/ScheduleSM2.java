package hdnguyen.algorithm;



public class ScheduleSM2 {
    public static OutputSm2 calc(InputSm2 inputSm2) {
        OutputSm2 outputSm2 = new OutputSm2();
        if (inputSm2.getQ() >= 3 ) { // correct response
            if (inputSm2.getN() == 0) {
                outputSm2.setI(1);
            }
            else if (inputSm2.getN() == 1){
                outputSm2.setI(6);
            }
            else {
                outputSm2.setI((int)(inputSm2.getI() * inputSm2.getEf()));
            }
            outputSm2.setN(inputSm2.getN() + 1);
        }
        else { // không nhớ.
            outputSm2.setN(0);
            outputSm2.setI(1);
        }
        int q = inputSm2.getQ();
        outputSm2.setEf((float) (inputSm2.getEf() + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))));
        if (outputSm2.getEf() < 1.3f) {
            outputSm2.setEf(1.3f);
        }
        return outputSm2;
    }
}


