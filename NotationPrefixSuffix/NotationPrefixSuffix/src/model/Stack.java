package model;
import java.util.EmptyStackException; 
public class Stack {
    private Node top; 
    public Stack() {
        this.top = null;
    }
    public void push(Object element) {
        Node newNode = new Node(element); 
        newNode.setNext(this.top);       
        this.top = newNode;               
    }

    public boolean isEmpty() {
        return this.top == null;
    }

    public Object peek() throws EmptyStackException {
        if (this.top == null) {
            throw new EmptyStackException(); 
        }
        return this.top.getData(); 
    }
    public Object pop() throws EmptyStackException {
        if (this.top == null) {
            throw new EmptyStackException(); 
        }
        Object poppedElement = this.top.getData(); 
        this.top = this.top.getNext();             

        return poppedElement; 
    }

    public void clear() {
        this.top = null; 
    }
    
    public String displayForTrace() {
        StringBuilder sb = new StringBuilder(); 
        Node current = this.top;

        if (current == null) {
            sb.append("Vacia"); 
        } else {
            while (current != null) {
                sb.append(current.getData().toString());
                if (current.getNext() != null) {
                    sb.append(", "); 
                }
                current = current.getNext(); 
            }
        }
        return sb.toString(); 
        }
}