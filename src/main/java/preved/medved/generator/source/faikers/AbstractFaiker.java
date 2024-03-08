package preved.medved.generator.source.faikers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AbstractFaiker {
    protected ExecutorService executor;
    protected List<Runnable> dataFetchers = new ArrayList<>();

    protected void generateData(){
        this.dataFetchers.forEach(task -> executor.submit(task));
    };

    protected void fillBuffer(int cyclesNumber){
        for (int i = 0; i < cyclesNumber; i++) {
            this.generateData();
        }
    }
}
