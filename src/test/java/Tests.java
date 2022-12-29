import observer.ConcreteMember;
import observer.GroupAdmin;
import observer.Sender;
import observer.UndoableStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);
    // stub method to check external dependencies compatibility
    @Test
    public void test(){
        String s1 = "Alice";
        String s2 = "Bob";

        logger.info(()->JvmUtilities.objectFootprint(s1));

        logger.info(()->JvmUtilities.objectFootprint(s1,s2));

        logger.info(()->JvmUtilities.objectTotalSize(s1));

        logger.info(() -> JvmUtilities.jvmInfo());
    }
    GroupAdmin myGroup1 = new GroupAdmin();
    GroupAdmin myGroup2 = new GroupAdmin();
    ConcreteMember m1 = new ConcreteMember(myGroup1);
    ConcreteMember m2 = new ConcreteMember(myGroup1);

    @Test
    public void registingTest(){
        int numOfMembers = myGroup1.getMembers().size();
        m1.register(myGroup1);
        myGroup1.append("are you subscribe me?");
        assertEquals(m1.toString(),"Member: m1, USB: are you subscribe me?");
        assertEquals(numOfMembers+1,myGroup1.getMembers().size());
    }

    @Test
    public void unRegistingTest(){
        int numOfMembers = myGroup1.getMembers().size();
        m1.register(myGroup1);
        int afterRegisting = myGroup1.getMembers().size();
        m1.unregister(myGroup1);
        assertEquals(numOfMembers-1,afterRegisting);
    }

    @Test
    public void transitionsBetweenGroups(){
        m1.register(myGroup1);
        assertEquals("Member: m1, USB: ",m1.toString());

        myGroup1.append("are you subscribe me?");
        String currentUsb = m1.toString();
        // We want t check if after registing another Group the member will hold the previous value until acceptance of new Usb
        m1.register(myGroup2);
        assertEquals("Member: m1, USB: are you subscribe me?",currentUsb);

        myGroup2.append("yes!");
        currentUsb = m1.toString();
        assertEquals("Member: m1, USB: yes!",currentUsb);

//        myGroup2.delete(0,this.myGroup2.getUsb().toString().length());
        m1.unregister(myGroup2);
        currentUsb = m1.toString();
        assertEquals("Member: m1, USB is empty",currentUsb);
    }

    @Test
    void testAppend() {
        myGroup1.register(m1);
        myGroup1.append("check");
        myGroup1.append(myGroup1.getUsb().toString());
        assertEquals(myGroup1.getUsb().toString(),m1.getUsb().toString());
    }

    @Test
    void testDelete() {
        myGroup1.register(m1);
        myGroup1.append("You can't delete me!");
        myGroup1.delete(7,9);
        assertEquals(myGroup1.getUsb().toString(),m1.getUsb().toString());

        myGroup1.register(m2);
        myGroup1.append(" i was wrong");
        myGroup1.delete(0,m2.getUsb().toString().length());
        assertEquals("",m2.getUsb().toString());
    }

    @Test
    public void testInsert() {
        myGroup1.register(m1);
        myGroup1.append("Insert here please");
        int rowLength = myGroup1.getUsb().toString().length();
        myGroup1.insert(rowLength, ", ok!");
        int newLength = myGroup1.getUsb().toString().length();
        assertTrue(newLength == rowLength + 5);
    }

    @Test
    void testUndo() {
        myGroup1.register(m1);
        myGroup1.append("U can undo");
        myGroup1.append("!");
        myGroup1.undo();
        assertEquals("U can undo", m1.getUsb().toString());
        myGroup1.undo();
        assertEquals("", m1.getUsb().toString());
    }

    @Test
    void testUpdate() {
        myGroup1.register(m1);
        int rowLength = myGroup1.getUsb().toString().length();
        myGroup1.delete(0,rowLength);
        myGroup1.append("the usb updated");
        myGroup1.updateMembers();
        assertEquals("the usb updated", m1.getUsb().toString());

        UndoableStringBuilder usb = new UndoableStringBuilder();
        m1.update(usb);
        assertEquals(usb, m1.getUsb());
    }
}
