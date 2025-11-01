package model;

public class Node {
    
    Object data; 
 
    private Node next;
    public Node(Object x) {
        this.data = x;
        this.next = null; 
    }
    
    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }
    
    public Object getData() {
        return this.data; 
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNext() {
        return this.next; 
    }

    public void setNext(Node next) {
        this.next = next;
    }
}