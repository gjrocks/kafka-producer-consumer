package com.gj.kafka.employees;

public class EmployeeStreamWorker implements Runnable {

    String city;
    public EmployeeStreamWorker(String city) {
        this.city = city;
    }

    @Override
    public void run() {

        new EmployeeStreamProcessor().empStream(city);
    }
}
