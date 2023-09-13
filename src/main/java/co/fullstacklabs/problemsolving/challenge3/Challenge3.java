package co.fullstacklabs.problemsolving.challenge3;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge3 {
    private final int[][] board;
    private final Node source = new Node(0,0), destination;

    int width() {
        return board[0].length;
    }
    int height() {
        return board.length;
    }

    @AllArgsConstructor
    class Path implements Visit {
        final List<Node> nodes ;

        Path() {
            nodes = new ArrayList<>(width()*height());
        }
        public int cost() {
            return nodes.stream().mapToInt(Node::value)
                    .sum();
        }
        public Visit copy() {
            return new Path(new ArrayList<>(this.nodes));
        }
        public void append(Node n) {
            nodes.add(n);
        }
        public Node tail() {
            return nodes.get(nodes.size() - 1);
        }

        public boolean contains(Node node) {
            return nodes.contains(node);
        }
    }

    @Data
    @AllArgsConstructor
    class Node {
        final int x, y;

        Node up() {
            return new Node(x, y-1);
        }
        Node down() {
            return new Node(x, y+1);
        }
        Node left() {
            return new Node(x-1, y);
        }
        Node right() {
            return new Node(x+1,y);
        }
        public String toString() {
            return ""+this.value();//""["+x+ ", "+y+"]";
        }

        int value() {
            return board[y][x];
        }
        List<Node> neighbours() {
            return Stream.of(up(), down(), left(), right())
                    .filter(n -> 0 <= n.getX() && n.getX() < width())
                    .filter(n -> 0 <= n.getY() && n.getY() < height())
                    .collect(toList());
        }
    }

    public Challenge3(int[][] board) {
        this.board = board;
        destination = new Node(width()-1, height()-1);
    }

    public static int findLessCostPathOld(int[][] board) {
        Challenge3 challenge = new Challenge3(board);
        Visit path = challenge.leastCostPath();
        return path.cost()-challenge.destination.value();
    }

    public static int findLessCostPath(int[][] board) {
        Challenge3 challenge = new Challenge3(board);
        return challenge.dijkstra()+challenge.source.value()-challenge.destination.value();
    }
    Visit leastCostPath() {
        return minPath(new Path(), source);
    }

    int dijkstra() {
        Pair<Map<Node, Integer>, Map<Node, Node>> dijkstra = dijkstra(source);
        return dijkstra.getFirst().get(destination);
    }

    Visit minPath(Visit visit, Node node) {
        visit.append(node);
        if (node.equals(destination))
            return visit;
        return from(visit, node)
                .stream().parallel()
                .filter(n -> !visit.contains(n))
                .map(next -> minPath(visit.copy(), next))
                .filter(path1 -> path1.contains(destination))
                .collect(toList()).stream()
                .min(comparingInt(Visit::cost)).orElseGet(Infinite::new);
    }
    class Infinite extends Path {
        public int cost() {
            return 500000;
        }
        public Node tail() {
            return new Node(-1, -1);
        }
    }
    List<Node> from(Visit visited, Node n) {
        return n.neighbours().stream()
                .filter(p -> !visited.contains(p))
                .collect(toList());
    }
    interface Visit {
        void append(Node n);
        Node tail();
        boolean contains(Node n);
        int cost();
        Visit copy();
    }
    Pair<Map<Node, Integer>, Map<Node, Node>> dijkstra(Node source) {
        Map<Node, Integer> distances = new HashMap<>();

        List<Node> vertices = new ArrayList<>();
        Map<Node, Node> prev = new HashMap<Node, Node>();
        for (int i=0; i<width(); i++) {
            for (int j=0; j<height(); j++) {
                vertices.add(new Node(i, j));
            }
        }
        List<Node> Q = new ArrayList<>(vertices);
        vertices.forEach(v -> distances.put(v, Integer.MAX_VALUE));
        distances.put(source, 0);
        while (!Q.isEmpty()) {
            Node u = Q.stream().min(comparingInt(v -> distances.get(v))).get();
            Q.remove(u);
            u.neighbours().stream()
                    .filter(v -> Q.contains(v))
                    .forEach(v -> {
                        int alt = distances.get(u)+v.value();
                        if (alt < distances.get(v)) {
                            distances.put(v, alt);
                            prev.put(v, u);
                        }
                    });
        }
        return Pair.of(distances, prev);
    }
    /*
     1  function Dijkstra(Graph, source):
 2
 3      for each vertex v in Graph.Vertices:
 4          dist[v] ← INFINITY
 5          prev[v] ← UNDEFINED
 6          add v to Q
 7      dist[source] ← 0
 8
 9      while Q is not empty:
10          u ← vertex in Q with min dist[u]
11          remove u from Q
12
13          for each neighbor v of u still in Q:
14              alt ← dist[u] + Graph.Edges(u, v)
15              if alt < dist[v]:
16                  dist[v] ← alt
17                  prev[v] ← u
18
19      return dist[], prev[]
     */
}
