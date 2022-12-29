package observer;

public class ConcreteMember implements Member {
    String memberName = "m"+(counter++);
    GroupAdmin adminSender;
    UndoableStringBuilder usb;
    static int counter = 1;

    public ConcreteMember(GroupAdmin adminSender) {
        register(adminSender);
    }

    public void register(GroupAdmin mySender) {
        if(mySender.members.contains(this))
            return;
        if((this.adminSender != null) && (this.adminSender != mySender)) {
            this.unregister(this.adminSender);
        }
        mySender.register(this);
        this.adminSender = mySender;
    }

    public void unregister(GroupAdmin adminSender) {
        adminSender.unregister(this);
        this.adminSender = null;
        this.usb = null;
    }

    @Override
    public void update(UndoableStringBuilder usb) {
        this.usb = usb;
    }

    @Override
    public String toString() {
        if(this.usb == null) {
            return "Member: "+this.memberName+", USB is empty";
        }
        return "Member: "+this.memberName+", USB: "+this.usb;
    }

    public void printGroup(GroupAdmin myGroup) {

    }

    public GroupAdmin getAdminSender() {
        return adminSender;
    }

    public UndoableStringBuilder getUsb() {
        return usb;
    }

    public String getMemberName() {
        return memberName;
    }


    public static void main(String[] args) {
        GroupAdmin myGroup1 = new GroupAdmin();
        GroupAdmin myGroup2 = new GroupAdmin();
        ConcreteMember m1 = new ConcreteMember(myGroup1);
        ConcreteMember m2 = new ConcreteMember(myGroup1);

        myGroup1.append("Hello ");
        myGroup1.append("World!");
        myGroup2.append("Goodbye");

//        m1.register(myGroup2);
        m1.register(myGroup2);
        m1.register(myGroup2);
        m1.register(myGroup2);
        m1.register(myGroup2);
//        m2.unregister(myGroup2);
//        m1.unregister(myGroup1);

//		System.out.println(myGroup1.toString());
        System.out.println(myGroup1.members);
        System.out.println(myGroup2.members);

    }

}