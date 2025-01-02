package com.gj.kafka.devices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.App;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtils
{

    public static JSONArray  readJson(){

        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("output1.json");
            String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(csvAsString);

            JSONArray arr=	json.getJSONArray("data");
           return arr;


        } catch (Exception e) {
            e.printStackTrace();
        }
    System.out.println("File is generated");
        return null;

    }

    public static JSONArray  readScedule(){

        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("schedule.json");
            String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(csvAsString);

            JSONArray arr=	json.getJSONArray("data");
            return arr;


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File is generated");
        return null;

    }

    public static JSONObject  readJsonReal(){

        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("mdo.json");
            String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(csvAsString);


            return json;


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File is generated");
        return null;

    }
    public static String method(String filePath)
    {

        // Declaring object of StringBuilder class
        StringBuilder builder = new StringBuilder();

        // try block to check for exceptions where
        // object of BufferedReader class us created
        // to read filepath
        try (BufferedReader buffer = new BufferedReader(
                new FileReader(filePath))) {

            String str;

            // Condition check via buffer.readLine() method
            // holding true upto that the while loop runs
            while ((str = buffer.readLine()) != null) {

                builder.append(str).append("\n");
            }
        }

        // Catch block to handle the exceptions
        catch (IOException e) {

            // Print the line number here exception occurred
            // using printStackTrace() method
            e.printStackTrace();
        }

        // Returning a string
        return builder.toString();
    }
    public static void generateJsonFromCSV(){


        InputStream inputStream = App.class.getClassLoader().getResourceAsStream("iot_telemetry_data.csv");
        String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        String json = CDL.toJSONArray(csvAsString).toString();
        String finalJson="{ \"data\":" +json+"}";
        try {
            Files.write(Path.of("src/main/resources/output1.json"), finalJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File is generated");
    }

    public static void generateData() throws  Exception{
        FileWriter file = new FileWriter("c:\\tmp\\rssi.csv");
        BufferedWriter bf = new BufferedWriter(file);
        bf.write("beacon,hub,rssi");
        bf.newLine();
       for(int i=0;i<100;i++){
           String beacon=beacons[getRandomNumber(0,beacons.length)];
           String hub=hubs[getRandomNumber(0,hubs.length)];
           int rssi=getRandomNumber(-20,-110);
           StringBuilder builder=new StringBuilder().append(beacon).append(",").append(hub).append(",").append(rssi);
           bf.write(builder.toString());
           bf.newLine();
       }

        bf.close();
    }

   public static String[] beacons=new String[]{"B1","B2","B3","B4","B5"};
    public static String[] hubs=new String[]{"H1","H2","H3"};


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    public static List<String> readDeviceJsonData() throws Exception{
        List<String> events=new ArrayList<>();
        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("devices.json");
            String event = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            //  System.out.println(csvAsString);
            ObjectMapper mapper = new ObjectMapper();
            //  try {
            JsonNode map = mapper.readValue(event, JsonNode.class);
            if (map.isArray()) {
                // System.out.println("here");
                for (JsonNode jsonNode : map) {
                    events.add(jsonNode.toString());
                }
            }
           //assertTrue(map.size()>0);
            System.out.println(map.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
