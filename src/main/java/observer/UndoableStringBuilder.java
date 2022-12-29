package observer;

import java.util.Stack;

/** This class creates an object of the type UndoableStringBuilder.
 * The class allows to add strings, delete, insert and replace substrings, as well as to reverse the string.
 * The UndoableStringBuilder (named USB) type contains two sub-types - a @StringBuilder (named stb) and a @Stack (named stk) which allows
 * to keep track of all modifications to the usb's string, and therefore undo operations (from last done to the first).
 * All methods @return the USB with its modified stb and stk and therefore the methods don't specify @return.
 * @author Ohad Wolfman and Benji Kehat
 * @version 2
 */

public class UndoableStringBuilder {
    StringBuilder stb = new StringBuilder();
    Stack<String> stk = new Stack<String>();

    /** This function adds given string to the end of the usb's string.
     * The new version of the string will be added to the usb's memory stack.
     ** Note that 'null' will be appended as a string.
     * @throws Error if argument is not a String type.
     *
     * @param str - the string being added to the usb's string.
     */
    public UndoableStringBuilder append(String str) {
        stb.append(str);
        stk.addElement(stb.toString());
        return this;
    }

    /** This Function deletes a substring from the @start index until the @end-1 index.
     * If end index is greater than the usb's string's length, the entire string will be deleted (no exception will be thrown).
     * If start=end, no substring will be deleted.
     * @throws StringIndexOutOfBoundsException if @start is a negative integer, if @start is greater than @end, or if @start
     * is greater than last index+1.
     *
     * @param start - index from which to start deleting.
     * @param end - first index in string after substring was deleted.
     */
    public UndoableStringBuilder delete(int start, int end) {
        stb.delete(start, end);
        stk.addElement(stb.toString());
        return this;
    }


    /** Inserts specified @string into the usb's string, starting at index @offset, and pushes forward the rest
     * of the string (all chars appearing after @offset will be of original index + @offset.
     * Adds updated usb string to the usb stack.
     * @throws StringIndexOutOfBoundsException if @start is a negative integer, or if @start
     * is greater than last index+1.
     *
     * @param offset - index from which substring is inserted.
     * @param str - sequence of chars to be inserted.
     */
    public UndoableStringBuilder insert(int offset, String str) {
        stb.insert(offset, str);
        stk.addElement(stb.toString());
        return this;
    }

    /** Replaces specified substring from @start index to @end index-1 from usb's string with specified @string.
     * Adds updated usb string to the usb stack.
     * @throws StringIndexOutOfBoundsException if @start is a negative integer, if @start is greater than the @end index,
     * or if @start is greater than last index+1.
     *
     * @param str - sequence of chars to be inserted instead of specified substring.
     * @param start - index from which substring is replaced.
     * @param end-1 - last index to be replaced.
     */
    public UndoableStringBuilder replace(int start, int end, String str) {
        stb.replace(start, end, str);
        stk.addElement(stb.toString());
        return this;
    }

    /** Reverses the order of the usb's string (last is first, first is last).
     */
    public UndoableStringBuilder reverse() {
        stb.reverse();
        stk.addElement(stb.toString());
        return this;
    }

    /** This function will undo the last operation done on the string. Running it several times will undo the operrations
     * from last to first.
     * The function is built on a stack memory of the modified usb string, as it was at every stage.
     * When all actions are undone, calling the function again will do nothing.
     */
    public void undo() {
        if (!stk.isEmpty()) {
            stk.pop();
            if (!stk.isEmpty()){
                stb.replace(0, stb.length(), stk.peek());
            } else {
                stb.replace(0, stb.length(), "");
            }
        }
    }

    /** This function prints the usb's current string.
     */
    @Override
    public String toString() {
        return stb.toString();
    }

    public static void main(String[] args) {
        UndoableStringBuilder usb = new UndoableStringBuilder();
        usb.append("Hello World!");

        System.out.println(usb.toString());
    }

}