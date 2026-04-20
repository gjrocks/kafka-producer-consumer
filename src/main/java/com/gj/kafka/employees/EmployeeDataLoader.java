package com.gj.kafka.employees;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.App;
import org.json.CDL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDataLoader {

    public static void generateJsonFromCSV(){


        InputStream inputStream = App.class.getClassLoader().getResourceAsStream("Employee.csv");
        String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        String json = CDL.toJSONArray(csvAsString).toString();
        //String finalJson="{ " +json+"}";
        try {
            Files.write(Path.of("src/main/resources/output_EmpoloyeeJson.json"), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File is generated");
    }

    //add main method to run this class independently
    public static void main(String[] args) throws Exception {
        //readEmployeeJsonData();
        generateJsonFromCSV();
    }

    public static List<String> readEmployeeJsonData() throws Exception{
        List<String> events=new ArrayList<>();
        try {
            InputStream inputStream = App.class.getClassLoader().getResourceAsStream("output_EmpoloyeeJson.json");
            String event = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            //  System.out.println(csvAsString);
            ObjectMapper mapper = new ObjectMapper();
            //  try {
            JsonNode map = mapper.readValue(event, JsonNode.class);
            if (map.isArray()) {
                // System.out.println("here");
                for (JsonNode jsonNode : map) {
//                    if(jsonNode.get("Age").asInt()>100){
//                        System.out.println("Invalid Age found for EmployeeID:  Age: " + jsonNode.get("Age").asInt());
//                       break;
//                    }
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
