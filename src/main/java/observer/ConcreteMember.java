package observer;

/** This class is an implementation of the Member.java interface,
 * to define the Observer type "ConcreteMember". A ConcreteMember
 * is a type of Member, with a name, a ({@code GroupAdmin} (see {@link}GroupAdmin.java}),
 * and a USB object which shallow copies the GroupAdmin's USB.
 * A ConcreteMember is able to register and unregister from a group
 * of ConcreteMembers, or get an update from the group's GroupAdmin
 * when it has changed its USB, in order to update its own USB. 
 * 
 * @authors Benji Kehat and Ohad Wolfman
 *
 */

public class ConcreteMember implements Member {
	String memberName;
	GroupAdmin adminSender;
	UndoableStringBuilder usb;
	
	// Returns this instance's GroupAdmin. Used for Tests class.
	public GroupAdmin getAdminSender() {
		return adminSender;
	}
	
	// Returns this instance's USB. Used for Tests class.
	public UndoableStringBuilder getUsb() {
		return usb;
	}
	
	// Returns this instance's name. Used for Tests class.
	public String getMemberName() {
	    return memberName;
	}
	
	// ConcreteMember constructor, that initializes the ConcreteMember and registers it to the specified GroupAdmin.
	public ConcreteMember(GroupAdmin adminSender, String name) {
		this.memberName = name;
		register(adminSender);
	}
	
	// Adds the ConcreteMember to the specified GroupAdmin's members list, and shallow copies its USB.
	// The ConcreteMember's adminSender field will now point to specified GroupAdmin.
	// If the ConcreteMember is already registered to the GroupAdmin, nothing will happen.
	// If the ConcreteMember is already registered to a different GroupAdmin, it will first unregister from it
	// and only then register the new one.
	public void register(GroupAdmin mySender) {
		if((this.adminSender != null) && (this.adminSender != mySender)) {
			this.unregister(this.adminSender);
		}
		if(this.adminSender == mySender) {
			return;
		}
		mySender.members.add(this);
		this.adminSender = mySender;
		this.usb = mySender.usb;
		System.out.println("New member added");
	}
	
	// Removes the ConcreteMember from the specified GroupAdmin's members list.
	// The ConcreteMember's USB and adminSender will become null.
	// If the ConcreteMember attempts to unregister from a GroupAdmin it doesn't belong to, nothing will happen
	public void unregister(GroupAdmin mySender) {
		if(this.adminSender != mySender) {
			return;
		}
		mySender.members.remove(this);
		this.adminSender = null;
		this.usb = null;
		System.out.println("Member was removed");

	}
	
	// Copies the specified USB to the ConcreteMember's USB (shallow copy).
	@Override
	public void update(UndoableStringBuilder usb) {
		this.usb = usb;
	}
	
	// Returns a string representation of the ConcreteMember.
	@Override
	public String toString() {
		if(this.usb == null) {
			return "[Member: "+memberName+", USB is empty]";
		}
		return "[Member: "+memberName+", USB: "+this.usb.toString()+"]";
	}
	
//	public static void main(String[] args) {
//		GroupAdmin myGroup1 = new GroupAdmin();
//		GroupAdmin myGroup2 = new GroupAdmin();
//
//		ConcreteMember m1 = new ConcreteMember(myGroup1, "m1");
//		ConcreteMember m2 = new ConcreteMember(myGroup1, "m2");		
//		ConcreteMember m3 = new ConcreteMember(myGroup1, "m3");
//		
//		myGroup1.append("Hello ");
//		myGroup2.append("Goodbye");
//		
//		m3.register(myGroup2);
//		
//		myGroup1.append("World!");
//		
//		System.out.println(m1.toString());
//		System.out.println(m2.toString());
//		System.out.println(m3.toString());
//		myGroup1.printGroup();
//		myGroup2.printGroup();
//	}
	
}
