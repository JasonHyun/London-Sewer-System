package diver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import graph.FindState;
import graph.FleeState;
import graph.Node;
import graph.NodeStatus;
import graph.SewerDiver;

public class McDiver extends SewerDiver {

    /** Find the ring in as few steps as possible. Once you get there, <br>
     * you must return from this function in order to pick<br>
     * it up. If you continue to move after finding the ring rather <br>
     * than returning, it will not count.<br>
     * If you return from this function while not standing on top of the ring, <br>
     * it will count as a failure.
     *
     * There is no limit to how many steps you can take, but you will receive<br>
     * a score bonus multiplier for finding the ring in fewer steps.
     *
     * At every step, you know only your current tile's ID and the ID of all<br>
     * open neighbor tiles, as well as the distance to the ring at each of <br>
     * these tiles (ignoring walls and obstacles).
     *
     * In order to get information about the current state, use functions<br>
     * currentLocation(), neighbors(), and distanceToRing() in state.<br>
     * You know you are standing on the ring when distanceToRing() is 0.
     *
     * Use function moveTo(long id) in state to move to a neighboring<br>
     * tile by its ID. Doing this will change state to reflect your new position.
     *
     * A suggested first implementation that will always find the ring, but <br>
     * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
     * Some modification is necessary to make the search better, in general. */
    @Override
    public void find(FindState state) {
        // TODO : Find the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, flee.
        ArrayList<Long> list= new ArrayList<>();

        if (state.distanceToRing() != 0) {
            // dfs(list, state);
            find2(list, state);
        }

    }

    /** A simple depth-first walk implementation. The function marks the current node as visited,
     * and moves on to its neighbors to mark unvisited neighbors as visited. McDiver is moved to the
     * unvisited neighbor each time, until he reaches the tile with the ring and returns. This
     * functions does not include optimizations. */
    public void dfs(ArrayList<Long> list, FindState state) {

        if (state.distanceToRing() == 0) { return; }
        long initial= state.currentLocation();
        list.add(initial);

        for (NodeStatus n : state.neighbors()) {
            if (!list.contains(n.getId())) {
                state.moveTo(n.getId());
                list.add(state.currentLocation());
                dfs(list, state);

                if (state.distanceToRing() == 0) {
                    return;
                } else {
                    state.moveTo(initial);

                }

            }

        }
    }

    /** A modified depth-first walk implementation. The function marks the current node as visited,
     * and moves on to its sorted neighbors to mark unvisited neighbors as visited. McDiver is moved
     * to the unvisited neighbor each time, until he reaches the tile with the ring and returns. The
     * neighbors are sorted in nondecreasing order. This functions includes optimizations. */
    public void find2(ArrayList<Long> list, FindState state) {

        if (state.distanceToRing() == 0) { return; }
        long initial= state.currentLocation();
        list.add(initial);

        List<NodeStatus> sortedlist= (List<NodeStatus>) state.neighbors();
        Collections.sort(sortedlist);

        for (NodeStatus n : sortedlist) {
            if (!list.contains(n.getId())) {
                state.moveTo(n.getId());
                list.add(state.currentLocation());
                find2(list, state);

                if (state.distanceToRing() == 0) {
                    return;
                } else {
                    state.moveTo(initial);

                }

            }

        }

    }

    /** Flee --get out of the sewer system before the steps are all used, trying to <br>
     * collect as many coins as possible along the way. McDiver must ALWAYS <br>
     * get out before the steps are all used, and this should be prioritized above<br>
     * collecting coins.
     *
     * You now have access to the entire underlying graph, which can be accessed<br>
     * through FleeState. currentNode() and exit() will return Node objects<br>
     * of interest, and getNodes() will return a collection of all nodes on the graph.
     *
     * You have to get out of the sewer system in the number of steps given by<br>
     * stepToGo(); for each move along an edge, this number is <br>
     * decremented by the weight of the edge taken.
     *
     * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
     * When n is moved-to, coins on node n are automatically picked up.
     *
     * You must return from this function while standing at the exit. Failing <br>
     * to do so before steps run out or returning from the wrong node will be<br>
     * considered a failed run.
     *
     * Initially, there are enough steps to get from the starting point to the<br>
     * exit using the shortest path, although this will not collect many coins.<br>
     * For this reason, a good starting solution is to use the shortest path to<br>
     * the exit. */
    @Override
    public void flee(FleeState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        if (state.currentNode() == state.exit()) {} else {
            // leave(state);
            leave2(state);
        }

    }

    /** A simple shortest-path implementation from A6. The function creates a shortest possible path
     * between the current node and the exit, and has McDiver traverse along the path. The function
     * returns once McDiver reaches the exit. This function does not include optimizations. */
    public void leave(FleeState state) {
        if (state.currentNode() == state.exit()) {} else {
            List<Node> leavepath= A6.shortest(state.currentNode(), state.exit());
            for (Node n : leavepath) {
                if (n != state.currentNode()) {
                    state.moveTo(n);
                }
            }
        }
    }

    /** A modified shortest-path implementation from A6. The function calculates if it is possible
     * for McDiver to travel to a node with coins and make it to the exit without running out of
     * steps. If not, McDiver travels to the exit based on the first shortest path implementation.
     * The function returns once McDiver reaches the exit. This function includes optimizations. */
    public void leave2(FleeState state) {
        if (state.currentNode() == state.exit()) {} else {
            Collection<Node> map= state.allNodes();
            ArrayList<Node> map2= new ArrayList<>();
            ArrayList<Node> nodemap= new ArrayList<>();

            for (Node n : map) {
                map2.add(n);
            }
            for (Node m : map2) {
                if (m.getTile().coins() > 0) {
                    nodemap.add(m);
                }
            }
            nodemap.sort((b, c) -> c.getTile().coins() - b.getTile().coins());

            for (Node s : nodemap) {
                List<Node> currentToS= A6.shortest(state.currentNode(), s);
                int cToS= A6.pathSum(currentToS);
                List<Node> sToExit= A6.shortest(s, state.exit());
                int sToE= A6.pathSum(sToExit);

                int dist= cToS + sToE;

                if (dist < state.stepsToGo()) {

                    for (Node t : currentToS) {
                        List<Node> cToT= A6.shortest(state.currentNode(), t);
                        List<Node> tToExit= A6.shortest(t, state.exit());
                        int tSteps= A6.pathSum(tToExit) + A6.pathSum(cToT);

                        if (t != state.currentNode() && state.stepsToGo() < tSteps) {
                            leave(state);

                        } else if (t != state.currentNode()) {

                            state.moveTo(t);

                        }

                    }

                }

            }

        }
        leave(state);
    }

}
