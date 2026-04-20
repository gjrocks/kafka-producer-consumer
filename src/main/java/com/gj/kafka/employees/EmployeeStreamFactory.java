package com.gj.kafka.employees;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmployeeStreamFactory {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeStreamFactory.class);

    Map<String, Thread> employeeStreamWorkerMap = new HashMap<>();
    private  final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private  final ScheduledExecutorService autoHealer = Executors.newSingleThreadScheduledExecutor();
    @PostConstruct
    public void init() {
        logger.debug("EmployeeStreamFactory initialized");
        scheduler.execute(()->{
            try {
                while (true) {
                    for (Map.Entry<String, Thread> entry : employeeStreamWorkerMap.entrySet()) {
                        String city = entry.getKey();
                        Thread thread = entry.getValue();
                        logger.debug("Thread for city: " + city + " is alive: " + thread.isAlive());
//                        if (!thread.isAlive()) {
//                            logger.debug("Restarting stream worker for city: " + city);
//                            EmployeeStreamWorker employeeStreamWorker = new EmployeeStreamWorker(city);
//                            Thread newThread = new Thread(employeeStreamWorker);
//                            newThread.start();
//                            employeeStreamWorkerMap.put(city, newThread);
//                        }
                    }
                    Thread.sleep(10000); // Sleep for 10 seconds
                }
            } catch (Exception e) {
                logger.error("Cache refresh error::" + ExceptionUtils.getStackTrace(e));
            }
        });
        //scheduler.
//        scheduler.scheduleAtFixedRate(() -> {
//
//
//            try {
//            for (Map.Entry<String, Thread> entry : employeeStreamWorkerMap.entrySet()) {
//                String city = entry.getKey();
//                Thread thread = entry.getValue();
//               logger.debug("Thread for city: " + city + " is alive: " + thread.isAlive());
//                //if (!thread.isAlive()) {
//                  //  logger.debug("Restarting stream worker for city: " + city);
//                    //EmployeeStreamWorker employeeStreamWorker = new EmployeeStreamWorker(city);
//                    //Thread newThread = new Thread(employeeStreamWorker);
//                   // newThread.start();
//                    //employeeStreamWorkerMap.put(city, newThread);
//               //}
//            }
//            }catch (Exception e) {logger.error("Cache refresh error::" + ExceptionUtils.getStackTrace(e));}
//        }, 3,600, TimeUnit.SECONDS);

    }

    public void startEmployeeStream(String cities) {

        String[] cityArray = cities.split(",");
        for(String city : cityArray) {
             logger.debug("Starting stream for city: " + city);
            if (employeeStreamWorkerMap.containsKey(city)) {
                Thread thread = employeeStreamWorkerMap.get(city);
                if (thread!=null && thread.isAlive()) {
                    logger.debug("Stream worker for city " + city + " is already running.");
                    thread.interrupt();
                }


            }
            try {
                Thread.sleep(5000); // Wait for 5 seconds to ensure the previous thread has stopped
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            EmployeeStreamWorker employeeStreamWorker = new EmployeeStreamWorker(city);
            Thread thread = new Thread(employeeStreamWorker);
            thread.setName("EmployeeStreamWorker-" + city); //put the thread name unique
            thread.start();
            employeeStreamWorkerMap.put(city, thread);


        }


        try {
            Thread.sleep(20000); // wait for 20 seconds to ensure all streams have started
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        autoHealer.execute(()->{
            try {
                while (true) {
                    for (Map.Entry<String, Thread> entry : employeeStreamWorkerMap.entrySet()) {
                        String city = entry.getKey();
                        Thread thread = entry.getValue();
                        logger.debug("Thread for city: " + city + " is alive: " + thread.isAlive());
                        if (!thread.isAlive()) {
                            logger.debug("Restarting stream worker for city: " + city);
                            EmployeeStreamWorker employeeStreamWorker = new EmployeeStreamWorker(city);
                            Thread newThread = new Thread(employeeStreamWorker);
                            newThread.start();
                            employeeStreamWorkerMap.put(city, newThread);
                        }
                    }
                    Thread.sleep(20000); // Sleep for 20 seconds
                }
            } catch (Exception e) {
                logger.error("Cache refresh error::" + ExceptionUtils.getStackTrace(e));
            }
        });




    }


}
