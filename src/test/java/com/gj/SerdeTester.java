package com.gj;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.deserializer.RSSIAggrDeserializer;
import com.gj.kafka.deserializer.RSSIDeserializer;
import com.gj.kafka.model.Beacon;
import com.gj.kafka.model.RSSI;
import com.gj.kafka.serdes.CustomSerdesFactory;
import com.gj.kafka.serializer.RSSIAggreSerializer;
import com.gj.kafka.streams.aggregates.BeaconsAggregations;
import com.gj.kafka.streams.aggregates.RSSIAggregation;
import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
public class SerdeTester {


    //@Test
    public void rssiAggrTester() throws Exception {

        RSSIAggregation aggregation=new RSSIAggregation();
        RSSI first=new RSSI();
        first.setRssi(100);
        first.setBeacon("B1");
        first.setHub("H1");
        RSSI first1=new RSSI();
        first1.setRssi(34);
        first1.setBeacon("B1");
        first1.setHub("H2");

        aggregation.add(first);
        aggregation.add(first1);
        aggregation.print();
      /*  RSSIAggreSerializer serializer=new RSSIAggreSerializer();
        RSSIAggrDeserializer deserializer=new RSSIAggrDeserializer();
       byte[] aggrSerailised= serializer.serialize("rssi",aggregation);
        assertTrue(aggrSerailised!=null);
        RSSIAggregation deserialised=deserializer.deserialize("rssi",aggrSerailised);
        deserialised.print();*/
        Serde<RSSIAggregation> y= CustomSerdesFactory.rSSIAggregationSerde();
        byte[] aggrSerailised=y.serializer().serialize("rssi",aggregation);
        assertTrue(aggrSerailised!=null);
        RSSIAggregation deserialised=y.deserializer().deserialize("rssi",aggrSerailised);
        deserialised.print();
    }


    @Test
    public void beaconTester() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BeaconsAggregations aggregation=new BeaconsAggregations();
        for(int i=0;i<5;i++) {
            Beacon message = new Beacon("B" + i);
            message.setManufactuerer("B");
            aggregation.add(mapper.writeValueAsString(message));
        }


      /*  RSSIAggreSerializer serializer=new RSSIAggreSerializer();
        RSSIAggrDeserializer deserializer=new RSSIAggrDeserializer();
       byte[] aggrSerailised= serializer.serialize("rssi",aggregation);
        assertTrue(aggrSerailised!=null);
        RSSIAggregation deserialised=deserializer.deserialize("rssi",aggrSerailised);
        deserialised.print();*/
        Serde<BeaconsAggregations> y= CustomSerdesFactory.beaconAggregationSerde();
        byte[] aggrSerailised=y.serializer().serialize("rssi",aggregation);
        assertTrue(aggrSerailised!=null);
        BeaconsAggregations deserialised=y.deserializer().deserialize("rssi",aggrSerailised);
        assertTrue(deserialised.getBeacons().size()==5);

    }
}
