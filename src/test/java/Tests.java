import observer.ConcreteMember;
import observer.GroupAdmin;
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
    
    @Test
    public void updateConcreteMemberTest(){
    	GroupAdmin myGroup = new GroupAdmin();
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1"); // changes group size to 1.
    	myGroup.getUsb().append("Will not show in m1's string");
    	UndoableStringBuilder usb = new UndoableStringBuilder();
    	usb.append("This will show in m1's string");
    	m1.update(usb);
    	assertNotEquals(myGroup.getUsb().toString(), m1.getUsb().toString());
    	assertEquals("This will show in m1's string", m1.getUsb().toString());
    }
    
    @Test
    void updateMembersTest() {
    	GroupAdmin myGroup = new GroupAdmin();
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1"); // changes group size to 1.
    	ConcreteMember m2 = new ConcreteMember(myGroup, "m2"); // changes group size to 1.
    	myGroup.append("The GroupAdmin's USB was modified");
    	myGroup.updateMembers();
    	assertEquals("The GroupAdmin's USB was modified", m1.getUsb().toString());
    	assertEquals("The GroupAdmin's USB was modified", m2.getUsb().toString());
    }
    
    @Test
    public void registerConcreteMemberMethodTest(){
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("Hello ");
		int numOfMembers = myGroup.getMembers().size(); // 0 at this point.
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1"); // changes group size to 1.
        assertTrue(myGroup.getMembers().contains(m1));
        assertEquals(myGroup.getMembers().size(), numOfMembers+1);
        assertEquals(m1.toString(),"[Member: m1, USB: Hello ]");
        myGroup.append("World!"); // updates m1 usb
        assertEquals(m1.toString(),"[Member: m1, USB: Hello World!]");
        m1.register(myGroup); // m1 is already registered to myGroup, should do nothing.
        assertEquals(myGroup.getMembers().size(), numOfMembers+1);

	}
    
    @Test
    public void registerGroupAdminMethodTest(){
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("Hello ");
		int numOfMembers = myGroup.getMembers().size(); // 0 at this point.
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1"); // initialize m1.
    	m1.unregister(myGroup); // unregister m1.
    	myGroup.register(m1); // test GroupAdmin.register() method.
        assertTrue(myGroup.getMembers().contains(m1));
        assertEquals(myGroup.getMembers().size(), numOfMembers+1);
        assertEquals(m1.toString(),"[Member: m1, USB: Hello ]");
        myGroup.append("World!"); // updates m1 usb
        assertEquals(m1.toString(),"[Member: m1, USB: Hello World!]");
        myGroup.register(m1); // m1 is already registered to myGroup, should do nothing.
        assertEquals(myGroup.getMembers().size(), numOfMembers+1);
	}

    @Test
    public void unregisterConcreteMemberMethodTest(){
    	GroupAdmin myGroup = new GroupAdmin();
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1");
        int numOfMembers = myGroup.getMembers().size(); // 1 at this point.
        m1.unregister(myGroup); // changes group size to 0.
        assertFalse(myGroup.getMembers().contains(m1)); // myGroup no longer contains m1.
        assertEquals(myGroup.getMembers().size(), numOfMembers-1); // 0 at this point.
        assertEquals(m1.toString(),"[Member: m1, USB is empty]"); // m1 usb should be empty.
        m1.unregister(myGroup); // m1 is already unregistered from myGroup, should do nothing.
        assertEquals(myGroup.getMembers().size(), numOfMembers-1);
    }
    
    @Test
    public void unregisterGroupAdminMethodTest(){
    	GroupAdmin myGroup = new GroupAdmin();
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1");
        int numOfMembers = myGroup.getMembers().size(); // 1 at this point.
        myGroup.unregister(m1); // changes group size to 0.
        assertFalse(myGroup.getMembers().contains(m1)); // myGroup no longer contains m1.
        assertEquals(myGroup.getMembers().size(), numOfMembers-1); // 0 at this point.
        assertEquals(m1.toString(),"[Member: m1, USB is empty]"); // m1 usb should be empty.
        myGroup.unregister(m1); // m1 is already unregistered from myGroup, should do nothing.
        assertEquals(myGroup.getMembers().size(), numOfMembers-1);
    }
    
    @Test
    public void transitionsBetweenGroupsTest(){
    	GroupAdmin myGroup = new GroupAdmin();
    	myGroup.append("Good guys");
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1");
        int numOfMembers = myGroup.getMembers().size(); // 1 at this point.
        GroupAdmin myGroup2 = new GroupAdmin();
        myGroup2.append("Bad guys");
    	m1.register(myGroup2); // will unregister m3 from myGroup before registering with myGroup2.
    	assertFalse(myGroup.getMembers().contains(m1));
    	assertEquals(myGroup.getMembers().size(), numOfMembers-1);
    	assertEquals("[Member: m1, USB: Bad guys]", m1.toString());
    	myGroup.insert(0, "You still belong to the good guys"); // if m1 is still getting updates from myGroup, its string will change.
    	assertEquals("[Member: m1, USB: Bad guys]", m1.toString());
    }

    @Test
    void testAppend() {
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("Test the");
        assertEquals(myGroup.getUsb().toString(), "Test the");
        myGroup.append(" append method");
        assertEquals(myGroup.getUsb().toString(), "Test the append method");
    }

    @Test
    void testDelete() {
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("You can't delete me!");
        myGroup.delete(7,9);
        assertEquals(myGroup.getUsb().toString(),"You can delete me!");
        myGroup.delete(1,1);
        assertEquals(myGroup.getUsb().toString(),"You can delete me!");
        myGroup.delete(3,20);
        assertEquals(myGroup.getUsb().toString(),"You");
    }

    @Test
    public void testInsert() {
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("Insert here");
        myGroup.insert(7, "a string ");
        assertEquals(myGroup.getUsb().toString(),"Insert a string here");
    }

    @Test
    void testUndo() {
    	GroupAdmin myGroup = new GroupAdmin();
        myGroup.append("Append a string");
        myGroup.append(", and then append another string");
        assertEquals(myGroup.getUsb().toString(),"Append a string, and then append another string");
        myGroup.undo();
        assertEquals(myGroup.getUsb().toString(),"Append a string");
    }
    
    @Test
    void memoryTest() {
    	GroupAdmin myGroup = new GroupAdmin();
    	ConcreteMember m1 = new ConcreteMember(myGroup, "m1");
    	ConcreteMember m2 = new ConcreteMember(myGroup, "m1");

    	System.out.println("\nEmpty USB:\n");
    	System.out.println("myGroup: " + JvmUtilities.objectFootprint(myGroup));
    	System.out.println("m1: " + JvmUtilities.objectFootprint(myGroup));
    	System.out.println("m1: " + JvmUtilities.objectTotalSize(m1));
    	System.out.println("m2: " + JvmUtilities.objectTotalSize(m2));
    	
    	myGroup.append("String added");
    	
    	System.out.println("\nUSB With String:\n");
    	System.out.println("myGroup: " + JvmUtilities.objectFootprint(myGroup));
    	System.out.println("m1: " + JvmUtilities.objectFootprint(myGroup));
    	System.out.println("m1: " + JvmUtilities.objectTotalSize(m1));
    	System.out.println("m2: " + JvmUtilities.objectTotalSize(m2));
    	System.out.println("\nProgram total: " + JvmUtilities.jvmInfo());
    } 
}