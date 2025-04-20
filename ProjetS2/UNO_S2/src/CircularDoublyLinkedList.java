public class CircularDoublyLinkedList {
    // Private inner class to represent a node in the linked list
    private class Node {
        private Player player; // Player stored in the node
        private Node next;     // Reference to the next node
        private Node prev;     // Reference to the previous node

        // Constructor to initialize a node with a player
        Node(Player player) {
            this.player = player;
        }
    }

    private Node head;  // First node in the list
    private Node tail;  // Last node in the list
    private Node current; // Current node (for traversal)
    private int size;   // Number of nodes in the list

    // Get the first player in the list
    public Player getFirstPlayer() {
        if (head == null) return null;
        current = head;
        return head.player;
    }

    // Get the next player without moving the current pointer
    public Player getnext() {
        if (current == null || head == null) return null;
        return current.next.player;
    }
    
    // Get the previous player without moving the current pointer
    public Player getprev() {
        if (current == null || head == null) return null;
        return current.prev.player;
    } 
    
    // Move to the next player and return them
    public Player next() {
        if (head == null || current == null) {
            return null;
        }
        current = current.next;
        return current.player;
    }
    
    // Move to the previous player and return them
    public Player prev() {
        if (head == null || current == null) {
            return null;
        }
        current = current.prev;
        return current.player;
    }

    // Add a player to the list
    public void add(Player player) {
        Node newNode = new Node(player);

        if (head == null) {
            // If the list is empty, set the new node as head and tail
            head = newNode;
            head.next = head;
            head.prev = head;
            tail = head;
            current = head;
        } else {
            // Add the new node at the end of the list
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
            tail = newNode; // Update tail to the new node
        }
        size++; // Increment the size of the list
    }
    public void setCurrentPlayer(Player player) {
        Node temp = head;
        do {
            if (temp.player.equals(player)) {
                current = temp;
                return;
            }
            temp = temp.next;
        } while (temp != head);
    }
    // Get the number of players in the list
    public int getSize() {
        return this.size;
    }
    public Player getNextPlayer(Player currentPlayer) {
        Node temp = head;
        do {
            if (temp.player.equals(currentPlayer)) {
                return temp.next.player;
            }
            temp = temp.next;
        } while (temp != head);
        return null;
    }

    public Player getPreviousPlayer(Player currentPlayer) {
        Node temp = head;
        do {
            if (temp.player.equals(currentPlayer)) {
                return temp.prev.player;
            }
            temp = temp.next;
        } while (temp != head);
        return null;
    }
    public void printList() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }

        System.out.println("Current list state (size: " + size + "):");
        
        Node start = current != null ? current : head;
        Node temp = start;
        
        int count = 0;
        do {
            System.out.println("  " + (temp == current ? "->" : "  ") + 
                             " Player: " + (temp.player != null ? temp.player.getname() : "NULL") +
                             " (Node: " + System.identityHashCode(temp) + ")");
            temp = temp.next;
            count++;
            
            // Safety check to prevent infinite loops
            if (count > size * 2) {
                System.err.println("ERROR: Possible circular reference issue!");
                break;
            }
        } while (temp != start);

        // Print current node info
        System.out.println("Current node: " + 
                          (current != null ? 
                           current.player.getname() + " (" + System.identityHashCode(current) + ")" : 
                           "NULL"));
    }
}