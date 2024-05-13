package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    LinkedHashMap map;
    LinkedList list;

    private static class Node { // появилось
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    HashMap<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    public void add(Task task) {
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    public void removeFromHistory(int id) {
        Node node = history.get(id);
        removeNode(node);
    }

    void linkLast(Task task) { //дописать
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }

    private void removeNode(Node node) {
        //TODO
        Node prevNode = node.prev;
        Node nextNode = node.next;
        //Если удаляется первая нода
        if (prevNode == null) {
            first = nextNode;
            nextNode.prev = null;
            history.remove(node.item.getId());
            return;
        }
        //Если удаляется последняя нода
        if (nextNode == null) {
            last = prevNode;
            prevNode.next = null;
            history.remove(node.item.getId());
            return;
        }
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        history.remove(node.item.getId());

    }

    // Реализация метода getHistory должна перекладывать задачи из связного списка
    // в ArrayList для формирования ответа.
    public List<Task> getHistory() {//
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }
}
