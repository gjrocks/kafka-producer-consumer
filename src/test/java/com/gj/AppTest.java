package com.gj;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.serializer.CitySerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateAclsResult;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourceType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
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

    }
}
