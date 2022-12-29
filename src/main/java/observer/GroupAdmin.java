package observer;

import java.util.ArrayList;
import java.util.List;

public class GroupAdmin implements Sender {
    List<Member> members = new ArrayList<>();
    UndoableStringBuilder usb = new UndoableStringBuilder();



    public List<Member> getMembers() {
        return members;
    }

    public UndoableStringBuilder getUsb() {
        return usb;
    }

    public void updateMembers() {
        for(Member member : this.members) {
            member.update(this.usb);
        }
    }

    @Override
    public void register(Member obj) {
        this.members.add(obj);
        obj.update(this.usb);
        System.out.println("New member added to group");
    }

    @Override
    public void unregister(Member obj) {
        this.members.remove(obj);
        System.out.println("Member removed from group");
    }

    @Override
    public void insert(int offset, String obj) {
        this.usb.insert(offset, obj);
        updateMembers();
    }

    @Override
    public void append(String obj) {
        this.usb.append(obj);
        updateMembers();
    }

    @Override
    public void delete(int start, int end) {
        this.usb.delete(start, end);
        updateMembers();
    }

    @Override
    public void undo() {
        this.usb.undo();
        updateMembers();
    }

    public String toString() {
        return this.usb.toString();
    }

//	public void printMembers() {
//		for(ConcreteMember member : this.members) {
//			System.out.println(member.toString());;
//		}
//	}

}