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

        // Getter for the next player
        public Player nextPlayer() {
            return this.next.player;
        }
    }

    private Node head;  // First node in the list
    private Node tail;  // Last node in the list
    private Node current; // Current node (for traversal)
    private int size;   // Number of nodes in the list

    // Get the first player in the list
    public Player getFirstPlayer() {
        this.current = this.head;
        return this.head.player;
    }

    // Move to the next player and return them
    public Player next() {
        this.current = this.current.next;
        return this.current.player;
    }

    // Move to the previous player and return them
    public Player prev() {
        this.current = this.current.prev;
        return this.current.player;
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

    // Remove a player from the list
    public void remove(Player player) {
        if (head == null) {
            return; // List is empty, nothing to remove
        }

        Node p = head;

        // Handle the case where there's only one node in the list
        if (p.next == p) {
            if (p.player.equals(player)) {
                head = null;
                tail = null;
                size--;
            }
            return;
        }

        // Traverse the list to find and remove the player
        do {
            if (p.player.equals(player)) {
                // Update the next and prev references of adjacent nodes
                p.prev.next = p.next;
                p.next.prev = p.prev;

                // Update head or tail if necessary
                if (p == head) {
                    head = p.next;
                }
                if (p == tail) {
                    tail = p.prev;
                }

                // Update current if it points to the removed node
                if (p == current) {
                    current = p.next;
                }

                size--; // Decrement the size of the list
                return;
            }
            p = p.next; // Move to the next node
        } while (p != head); // Loop until we return to the head
    }

    // Get the number of players in the list
    public int getSize() {
        return this.size;
    }
}