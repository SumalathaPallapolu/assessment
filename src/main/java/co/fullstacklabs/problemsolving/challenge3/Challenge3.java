package co.fullstacklabs.problemsolving.challenge3;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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
    private final Node destination;

    int width() {
        return board[0].length;
    }
    int height() {
        return board.length;
    }

    @AllArgsConstructor
    class Path {
        final List<Node> nodes ;

        Path() {
            nodes = new ArrayList<>();
        }
        int cost() {
            return nodes.stream().mapToInt(Node::value)
                    .sum();
        }
        Path copy() {
            return new Path(new ArrayList<>(this.nodes));
        }
        public String toString() {
            return "["+nodes.stream().map(n -> n.toString()).collect(joining(", "))+"]";
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
    }

    public Challenge3(int[][] board) {
        this.board = board;
        destination = new Node(width()-1, height()-1);
    }

    public static int findLessCostPath(int[][] board) {
        Challenge3 challenge = new Challenge3(board);
        Path path = challenge.leastCostPath();
        return path.cost()-challenge.destination.value();
    }
    /*
    public static int findLessCostPathPopo(int[][] board) {
        return new Challenge3(board).leastCostFromStart();
    }


    int leastCostFromStart() {
        return leastCostFrom(new Path(new ArrayList<>()), new Node(0, 0));
    }
    int leastCostFrom(Path visited, Node node) {
        visited.nodes.add(node);
        System.out.print("p: "+visited+", visiting "+node);
        if (node.equals(destination)) {
            System.out.println("loto: "+destination);
            return node.value();
        }
        Integer minFrom = from(visited, node)
                .stream()
                .map(next -> leastCostFrom(visited.copy(), next))
                .min((a, b) -> a-b)
                .orElse(node.value());
        System.out.println(", min from "+node+": "+minFrom);
        return node.value()+ minFrom;
    }
 */
    Path leastCostPath() {
        return visit(new Path(), new Node(0, 0))
                .stream()
                .filter(path1 -> path1.nodes.get(path1.nodes.size() - 1).equals(destination))
                .collect(toList()).stream()
                .min(comparingInt(Path::cost)).get();
    }

    List<Path> visit(Path path, Node node) {
        path.nodes.add(node);
        if (node.equals(destination))
            return List.of(path);
        return from(path, node)
                .stream().flatMap(next -> visit(path.copy(), next).stream())
                .collect(toList());
    }
    List<Node> from(Path visited, Node n) {
        return Stream.of(n.up(), n.down(), n.left(), n.right())
                .filter(p -> 0 <= p.getX() && p.getX() < width())
                .filter(p -> 0 <= p.getY() && p.getY() < height())
                .filter(p -> !visited.nodes.contains(p))
                .collect(toList());
    }
}
