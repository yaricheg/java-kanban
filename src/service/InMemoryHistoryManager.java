package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private static class Node {
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
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public void removeFromHistory(int id) {
        Node node = history.get(id);
        removeNode(node);
    }

    // Реализация метода getHistory должна перекладывать задачи из связного списка
    // в ArrayList для формирования ответа.
    @Override
    public List<Task> getHistory() {
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }


    private void linkLast(Task task) {
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
        if (node == null) {
            System.out.println("Нода не найдена");
            return;
        }
        Node prevNode = node.prev;
        Node nextNode = node.next;
        //Если удаляется первая нода
        if (prevNode == null) {
            first = nextNode;
            nextNode.prev = null;
        }
        //Если удаляется последняя нода
        else if (nextNode == null) {
            last = prevNode;
            prevNode.next = null;

        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        history.remove(node.item.getId());
    }
}



