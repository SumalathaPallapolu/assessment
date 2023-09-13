package co.fullstacklabs.problemsolving.challenge3;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final Node destination;

    int width() {
        return board[0].length;
    }
    int height() {
        return board.length;
    }

    @AllArgsConstructor
    class Path implements Visit {
//        final List<Node> nodes ;
        private final boolean[][] belongs;

        Path() {
            belongs = new boolean[height()][width()];
        }
        public int cost() {
            return IntStream.range(0, height())
                    .boxed()
                    .flatMap(y -> IntStream.range(0, width())
                            .boxed()
                            .map(x -> new Node(x, y)))
                    .filter(n -> belongs[n.y][n.x])
                    .mapToInt(n -> n.value())
                     .sum();
        }
        public Visit copy() {
//            Arrays.cop
            Path p = new Path();
            for (int i = 0; i < belongs.length; i++)
                p.belongs[i] = Arrays.copyOf(belongs[i], belongs[i].length);
            return p;
        }
        /*
        public String toString() {
            return "["+nodes.stream().map(Node::toString).collect(joining(", "))+"]";
        }

         */

        public void append(Node n) {
            belongs[n.getY()][n.getX()] = true;
        }

        public Node tail() {
            return null;
            // IntStream.range(0, height()).boxed().flatMap(y -> IntStream.range(0, width()).boxed().map(x -> new Node(x, y)));
            // return nodes.get(nodes.size() - 1);
        }

        public boolean contains(Node node) {
            return belongs[node.getY()][node.getX()];
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
        Visit path = challenge.leastCostPath();
        return path.cost()-challenge.destination.value();
    }

    Visit leastCostPath() {
        return minPath(new Path(), new Node(0, 0));
    }

    Visit minPath(Visit visit, Node node) {
        visit.append(node);
        if (node.equals(destination))
            return visit;
        return from(visit, node)
                .stream().parallel().map(next -> minPath(visit.copy(), next))
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
        return Stream.of(n.up(), n.down(), n.left(), n.right())
                .filter(p -> 0 <= p.getX() && p.getX() < width())
                .filter(p -> 0 <= p.getY() && p.getY() < height())
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
}
