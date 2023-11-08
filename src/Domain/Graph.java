package Domain;

import java.util.*;

public class Graph {

    private Map<Long, List<Long>> adjacencyList;

    private Map<Long, Set<Long>> components;

    private Long nrOfComponents;

    public Graph(){

        this.adjacencyList = new HashMap<>();
        this.components = new HashMap<>();

    }

    public void addNode(Long node){

        adjacencyList.put(node, new LinkedList<>());

    }

    public void addEdge(Long source, Long destination){

        if(!adjacencyList.containsKey(source))
            addNode(source);
        if(!adjacencyList.containsKey(destination))
            addNode(destination);
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);

    }

    public void removeEdge(Long source, Long destination){

        for(Long neighbor : this.getNeighbors(source)){

            if(neighbor == destination) {
                adjacencyList.get(source).remove(neighbor);
                System.out.println(neighbor);
            }
        }

    }

    public void removeNodeAndNeighbors(Long source){

        for(Long node : this.adjacencyList.keySet()) {
            if(node != source)
               removeEdge(node, source);
        }
        this.adjacencyList.remove(source);

    }

    public List<Long> getNeighbors(Long node){

        if(adjacencyList.containsKey(node)){

            return adjacencyList.get(node);

        }
        return Collections.emptyList();

    }

    public Map<Long, List<Long>> getGraph(){

        return this.adjacencyList;

    }
    public void dfs(Long startNode) {
        Set<Long> visited = new HashSet<>();
        this.nrOfComponents = 0L;
        for(Long node : this.adjacencyList.keySet())
           if(!visited.contains(node)) {
               this.nrOfComponents += 1;
               dfsRecursive(node, visited);
           }
    }

    private void dfsRecursive(Long node, Set<Long> visited) {
        visited.add(node);
        if(this.components.get(this.nrOfComponents)  == null)
            this.components.put(this.nrOfComponents, new HashSet<>());
        this.components.get(this.nrOfComponents).add(node);

        for (Long neighbor : getNeighbors(node)) {
            if (!visited.contains(neighbor)) {
                dfsRecursive(neighbor, visited);
            }
        }
    }

    public Long getMinNode(){

        if (this.adjacencyList.isEmpty()) {
            return null;
        }

        Long minKey = Long.MAX_VALUE; // Initialize to a large value

        for (Long key : this.adjacencyList.keySet()) {
            if (key < minKey) {
                minKey = key;
            }
        }

        return minKey;

    }

    public Map<Long, Set<Long>> get_components(){

        if(this.getMinNode() != Long.MAX_VALUE)
            this.dfs(this.getMinNode());
        else
            return null;
        if(this.components.size() != 0){

            return this.components;

        }
        else
            return null;

    }

}
