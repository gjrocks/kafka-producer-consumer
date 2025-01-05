package com.gj;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.devices.CycleTimeEvent;
import com.gj.kafka.devices.DataUtils;
import com.gj.kafka.serializer.CitySerializer;
import freemarker.template.*;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.io.*;
import java.util.*;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
/**
 * Unit test for simple App.
 */


public class AppTest
{



    private static String[] tags={"Viscosity","Temperature","Pressure","Flow","Level","Density","Conductivity","Humidity","Position","Speed","Vibration","Current","Voltage","Power"};//,"Reactive Power","Apparent Power","Power Factor","Frequency","Active Energy","Reactive Energy","Apparent Energy","Active Demand","Reactive Demand","Apparent Demand","Active Demand Peak","Reactive Demand Peak","Apparent Demand Peak"};
    private static String[] tagsValues={"ProgramName","ViscosityValue","TemperatureValue","PressureValue","FlowValue","Level","Density","Conductivity","Humidity","Position","Speed","Vibration","Current","Voltage","Power"};//,"Reactive Power","Apparent Power","Power Factor","Frequency","Active Energy","Reactive Energy","Apparent Energy","Active Demand","Reactive Demand","Apparent Demand","Active Demand Peak","Reactive Demand Peak","Apparent Demand Peak"};
    private static String[] devices= {"Fanuc_PaintRobot_101", "Fanuc_PaintRobot_102", "Fanuc_PaintRobot_103"};// "Fanuc_PaintRobot_104", "Fanuc_PaintRobot_105", "Fanuc_PaintRobot_106", "Fanuc_PaintRobot_107", "Fanuc_PaintRobot_108"};//,"Fanuc_PaintRobot_109","Fanuc_PaintRobot_110","Fanuc_PaintRobot_111","Fanuc_PaintRobot_112","Fanuc_PaintRobot_113","Fanuc_PaintRobot_114","Fanuc_PaintRobot_115","Fanuc_PaintRobot_116","Fanuc_PaintRobot_117","Fanuc_PaintRobot_118","Fanuc_PaintRobot_119","Fanuc_PaintRobot_120","Fanuc_PaintRobot_121","Fanuc_PaintRobot_122","Fanuc_PaintRobot_123","Fanuc_PaintRobot_124","Fanuc_PaintRobot_125","Fanuc_PaintRobot_126","Fanuc_PaintRobot_127","Fanuc_PaintRobot_128","Fanuc_PaintRobot_129","Fanuc_PaintRobot_130","Fanuc_PaintRobot_131","Fanuc_PaintRobot_132","Fanuc_PaintRobot_133","Fanuc_PaintRobot_134","Fanuc_PaintRobot_135","Fanuc_PaintRobot_136","Fanuc_PaintRobot_137","Fanuc_PaintRobot_138","Fanuc_PaintRobot_139","Fanuc_PaintRobot_140","Fanuc_PaintRobot_141","Fanuc_PaintRobot_142","Fanuc_PaintRobot_143","Fanuc_PaintRobot_144","Fanuc_PaintRobot_145","Fanuc_PaintRobot_146","Fanuc_PaintRobot_147"};//,"Fanuc_PaintRobot_148","Fanuc_PaintRobot_149","Fanuc_PaintRobot_150","Fanuc_PaintRobot_151","Fanuc_PaintRobot_152","Fanuc_PaintRobot_153","Fanuc_PaintRobot_154","Fanuc_PaintRobot_155","Fanuc_PaintRobot_156","Fanuc_PaintRobot_157","Fanuc_PaintRobot_15
    private static String[] programNames={"Program1","Program2","Program3","Program4"};

    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public Long something(Long startdateEpochMilis,int interval) throws  Exception{

        Date startDate = new Date(startdateEpochMilis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        //System.out.println("Start Date: "+calendar.getTime());
        calendar.add(Calendar.MINUTE, interval);
       // System.out.println("end Date: "+calendar.getTime());
        return calendar.getTime().getTime();
    }

    //@Test
    public void testFTL() throws Exception {


        // 1. Configure FreeMarker
        //
        // You should do this ONLY ONCE, when your application starts,
        // then reuse the same Configuration object elsewhere.

        Configuration cfg = new Configuration();

        // Where do we load the templates from:
        cfg.setClassForTemplateLoading(AppTest.class, "/");

        // Some other recommended settings:
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // 2. Proccess template(s)
        //
        // You will do this for several times in typical applications.

        // 2.1. Prepare the template input:
        int count=0;
        String defaultValue="0.51002";
        Long timeStamp=1733900434215L;
     List<String> events=new ArrayList<>();

        for(int i=0;i<1600;i++) {

            Map<String, Object> input = new HashMap<String, Object>();
            input.put("deviceName", devices[getRandomNumberUsingInts(0, devices.length)]);
            if(count==0){
                input.put("tagName", "ProgramName");
                input.put("tagValue", "\""+ programNames[getRandomNumberUsingInts(0, programNames.length)]+"\"");
                count++;
            }else{
                input.put("tagName", tags[getRandomNumberUsingInts(0, tags.length)]);
                input.put("tagValue", defaultValue);
                count++;
            }

            input.put("timeStamp", timeStamp+"");
            Template template = cfg.getTemplate("device.ftl");

//            Writer consoleWriter = new OutputStreamWriter(System.out);
//            template.process(input, consoleWriter);

            try {

                Writer outWriter = new StringWriter();
                template.process(input, outWriter);
                String event=outWriter.toString();
                //System.out.println(event);
                events.add(event);

            } catch (IOException | TemplateException e) {
                throw new RuntimeException(e);
            }
            if(count>=10){
                count=0;
            }
            timeStamp= something(timeStamp,getRandomNumberUsingInts(5,27));
        }
        Map<String,JsonNode> externalContext=new HashMap<>();
        List<CycleTimeEvent> cycleTimeEvents=new ArrayList<>();
        events.stream().forEach(event->{
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode map = mapper.readValue(event, JsonNode.class);
                if(map!=null && map.get("tags").has("ProgramName")){
                    //System.out.println(map);
                    String deviceName=map.get("deviceName").asText();
                    if(externalContext.get(deviceName)!=null){
                        CycleTimeEvent cycleTimeEvent=new CycleTimeEvent();
                        JsonNode existingNode=externalContext.get(deviceName);
                        cycleTimeEvent.setDeviceName(deviceName);

                        JsonNode existingTags=existingNode.get("tags").get("ProgramName");
                         JsonNode existingProgramName=existingTags.get("tagValue");
                        JsonNode existingTime=existingTags.get("timestamp");
                        cycleTimeEvent.setPreviousProgramStartTime(existingTime.asLong());
                        cycleTimeEvent.setPreviousProgramName(existingProgramName.asText());
                        cycleTimeEvent.setCurrentProgramStartTime(map.get("tags").get("ProgramName").get("timestamp").asLong());
                        cycleTimeEvent.setProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                        cycleTimeEvent.setCurrentProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                        cycleTimeEvent.setCycleTime((cycleTimeEvent.getCurrentProgramStartTime()-cycleTimeEvent.getPreviousProgramStartTime())/(1000));
                        cycleTimeEvents.add(cycleTimeEvent);

                        externalContext.put(deviceName,map); //setting up new raw event as previous event
                    }else {
                        externalContext.put(deviceName, map);
                    }
                }
                //System.out.println(map);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
cycleTimeEvents.stream().forEach(System.out::println);
        // For the sake of example, also write output into a file:
//        Writer fileWriter = new FileWriter(new File("output.html"));
//        try {
//            template.process(input, fileWriter);
//        } finally {
//            fileWriter.close();
//        }


    }
    /**
     * Rigorous Test :-)
     */
   // @Test
    public void shouldAnswerWithTrue() throws Exception
    {
        //Properties config = new Properties();
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CitySerializer.class.getName());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");
        AdminClient admin = AdminClient.create(props);
       /* for (Node node : admin.describeCluster().nodes().get()) {
            System.out.println("-- node: " + node.id() + " --");
            ConfigResource cr = new ConfigResource(ConfigResource.Type.BROKER, "0");
            DescribeConfigsResult dcr = admin.describeConfigs(Collections.singleton(cr));
            dcr.all().get().forEach((k, c) -> {
                c.entries()
                        .forEach(configEntry -> {System.out.println(configEntry.name() + "= " + configEntry.value());});
            });
        }*/

        /*Collection<AclBinding> acls=  admin.describeAcls(AclBindingFilter.ANY).values().get();
        acls.stream().forEach(obj-> System.out.println(obj.entry().principal()));



        Collection<AclBinding> addAcls = new ArrayList<>();


        ResourceType resType = ResourceType.fromString("TOPIC");
        PatternType patType = PatternType.fromString("LITERAL");
        ResourcePattern resourcePattern = new ResourcePattern(resType, "test1", patType);

        AclOperation aclOp = AclOperation.fromString("CREATE");
        AclPermissionType aclPerm = AclPermissionType.fromString("ALLOW");

        AccessControlEntry accessControlEntry = new AccessControlEntry("User:test", "*", aclOp, aclPerm);

        AclBinding aclBinding = new AclBinding(resourcePattern, accessControlEntry);
        addAcls.add(aclBinding);

        try {
            final CreateAclsResult createAclsResult = admin.createAcls(addAcls);
            System.out.println("Ganesh :"+ createAclsResult.all().get());
        } catch (Exception e) {
            System.out.println(e);
        }*/
        assertTrue( true );

            String city="Los Angles";
      String newName=  city.replaceAll(" ","_").toLowerCase();
        System.out.println(newName);

    }


public void deriveCycleTime(List<String> events) throws Exception{
    Map<String,JsonNode> externalContext=new HashMap<>();
    List<CycleTimeEvent> cycleTimeEvents=new ArrayList<>();
    events.stream().forEach(event->{
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode map = mapper.readValue(event, JsonNode.class);
            if(map!=null && map.get("tags").has("ProgramName")){
               // System.out.println(map);
                String deviceName=map.get("deviceName").asText();
                if(externalContext.get(deviceName)!=null){
                    CycleTimeEvent cycleTimeEvent=new CycleTimeEvent();
                    JsonNode existingNode=externalContext.get(deviceName);
                    cycleTimeEvent.setDeviceName(deviceName);

                    JsonNode existingTags=existingNode.get("tags").get("ProgramName");
                    JsonNode existingProgramName=existingTags.get("tagValue");
                    JsonNode existingTime=existingTags.get("timestamp");
                    cycleTimeEvent.setPreviousProgramStartTime(existingTime.asLong());
                    cycleTimeEvent.setPreviousProgramName(existingProgramName.asText());
                    cycleTimeEvent.setCurrentProgramStartTime(map.get("tags").get("ProgramName").get("timestamp").asLong());
                    cycleTimeEvent.setProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                    cycleTimeEvent.setCurrentProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                    cycleTimeEvent.setCycleTime((cycleTimeEvent.getCurrentProgramStartTime()-cycleTimeEvent.getPreviousProgramStartTime())/(1000));
                    cycleTimeEvents.add(cycleTimeEvent);

                    externalContext.put(deviceName,map); //setting up new raw event as previous event
                }else {
                    externalContext.put(deviceName, map);
                }
            }
            //System.out.println(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    cycleTimeEvents.stream().forEach(System.out::println);
}

//@Test
    public void generateDeviceData() throws Exception{
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(AppTest.class, "/");
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        int count=0;
        String defaultValue="0.51002";
        Long timeStamp=1733900434215L;
        List<String> events=new ArrayList<>();
        FileWriter file = new FileWriter("c:\\tmp\\devices.json");
        BufferedWriter bf = new BufferedWriter(file);
    bf.write("[");
    bf.newLine();
        for(int i=0;i<1600;i++) {

            Map<String, Object> input = new HashMap<String, Object>();
            input.put("deviceName", devices[getRandomNumberUsingInts(0, devices.length)]);
            if(count==0){
                input.put("tagName", "ProgramName");
                input.put("tagValue", "\""+ programNames[getRandomNumberUsingInts(0, programNames.length)]+"\"");
                count++;
            }else{
                input.put("tagName", tags[getRandomNumberUsingInts(0, tags.length)]);
                input.put("tagValue", defaultValue);
                count++;
            }

            input.put("timeStamp", timeStamp+"");
            Template template = cfg.getTemplate("device.ftl");

//            Writer consoleWriter = new OutputStreamWriter(System.out);
//            template.process(input, consoleWriter);

            try {

                Writer outWriter = new StringWriter();
                template.process(input, outWriter);
                String event=outWriter.toString();
                bf.write(event);
                bf.write(",");
                bf.newLine();
                //System.out.println(event);
                events.add(event);

            } catch (IOException | TemplateException e) {
                throw new RuntimeException(e);
            }
            if(count>=10){
                count=0;
            }
            timeStamp= something(timeStamp,getRandomNumberUsingInts(5,27));
        }
    bf.write("]");
        bf.close();
        // For the sake of example, also write output into a file:
//        Writer fileWriter = new FileWriter(new File("output.html"));
//        try {
//            template.process(input, fileWriter);
//        } finally {
//            fileWriter.close();
//        }


    }

    //@Test


    @Test
    public void cycleTimeTest() throws Exception{
        List<String> events= DataUtils.readDeviceJsonData();
        deriveCycleTime(events);
    }
}
