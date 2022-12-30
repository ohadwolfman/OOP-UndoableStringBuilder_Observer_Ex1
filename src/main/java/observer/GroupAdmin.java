package observer;

import java.util.ArrayList;
import java.util.List;


/** This class is an implementation of the Sender.java interface, to define
 * the Observable type "GroupAdmin". A GroupAdmin is a type of Sender, with
 * a list of members and a USB. When a ConcreteMember registers to a GroupAdmin,
 * it is added to the members list, and will receive updates of changes being done
 * in the GroupAdmin's USB, thus changing its own USB. When the ConcreteMember
 * unregisters from the GroupAdmin, it is removed from the list and will stop getting updates.
 * Besides those two methods, the GroupAdmin implements the rest of the Sender interface's
 * methods (append, insert, delete and undo) to modify the current object's USB, using
 * the USB class's corresponding methods.
 *
 * @authors Benji Kehat and Ohad Wolfman
 *
 */

public class GroupAdmin implements Sender {
    List<ConcreteMember> members = new ArrayList<ConcreteMember>();
    UndoableStringBuilder usb = new UndoableStringBuilder();

    // Returns this instance's list of members. Used for Tests class.
    public List<ConcreteMember> getMembers() {
        return members;
    }

    // Returns this instance's USB. Used for Tests class.
    public UndoableStringBuilder getUsb() {
        return usb;
    }

    // Updates ConcreteMembers registered to the GroupAdmin with its current USB state.
    public void updateMembers() {
        for(Member member : this.members) {
            member.update(this.usb);
        }
    }

    // Casts the Member object to a ConcreteMember and calls its register() method,
    // with the current GroupAdmin instance as the argument.
    @Override
    public void register(Member obj) {
        if(this.members.contains((ConcreteMember)obj)) {
            return;
        }
        ((ConcreteMember)obj).register(this);
    }

    // Casts the Member object to a ConcreteMember and calls its unregister() method,
    // with the current GroupAdmin instance as the argument.
    @Override
    public void unregister(Member obj) {
        if(!this.members.contains((ConcreteMember)obj)) {
            return;
        }
        ((ConcreteMember)obj).unregister(this);
    }

    // Calls the insert() method of the GroupAdmin's USB.
    // Updates members calling the updateMembers() method.
    @Override
    public void insert(int offset, String obj) {
        this.usb.insert(offset, obj);
        updateMembers();
    }

    // Calls the append() method of the GroupAdmin's USB.
    // Updates members calling the updateMembers() method.
    @Override
    public void append(String obj) {
        this.usb.append(obj);
        updateMembers();
    }

    // Calls the delete() method of the GroupAdmin's USB.
    // Updates members calling the updateMembers() method.
    @Override
    public void delete(int start, int end) {
        this.usb.delete(start, end);
        updateMembers();
    }

    // Calls the undo() method of the GroupAdmin's USB.
    // Updates members calling the updateMembers() method.
    @Override
    public void undo() {
        this.usb.undo();
        updateMembers();
    }

    // Prints a string representation of the GroupAdmin's status:
    // The current USB string.
    // The registered members of the GroupAdmin, using the ConcreteMember's toString() method.
    public void printGroup() {
        System.out.println("USB is: "+this.usb.toString());
        if(this.members.isEmpty()) {
            System.out.println("There are no members in this group");
        } else {
            System.out.println("Group members are:");
            for(ConcreteMember member : this.members) {
                System.out.println(member.toString());
            }
        }
    }
}