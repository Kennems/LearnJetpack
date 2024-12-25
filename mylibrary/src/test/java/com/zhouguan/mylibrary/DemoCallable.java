package com.zhouguan.mylibrary;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 0;
    }
}

public class DemoCallable {
    @Test
    public void main() {
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        Thread t = new Thread(futureTask);

        t.start();

        try{
            Integer result = futureTask.get();
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
