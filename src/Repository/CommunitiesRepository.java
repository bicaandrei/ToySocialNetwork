package Repository;

import Domain.Graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommunitiesRepository {

    private Graph graph;

    public CommunitiesRepository() { this.graph = new Graph(); }

    public void add_user(Long node) { this.graph.addNode(node); }

    public void add_friendship(Long node1, Long node2) { this.graph.addEdge(node1, node2);}

    public void remove_user_from_communities(Long id) { this.graph.removeNodeAndNeighbors(id); }
    public Map<Long, Set<Long>> get_communites() { return this.graph.get_components(); }

}
