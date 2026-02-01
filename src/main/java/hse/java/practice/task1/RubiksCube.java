package hse.java.practice.task1;

import java.util.Arrays;

/**
 * Необходимо реализовать интерфейс Cube
 * При повороте передней грани, меняются верх низ право и лево
 */
public class RubiksCube  implements Cube{

    private static final int EDGES_COUNT    = 6;
	private static final int EDGE_SIZE      = 3;
	private static final int CENTER_INDEX   = 5;
	private static final int FIXED_ELEM     = 8;

    private final Edge[] edges = new Edge[EDGES_COUNT];
	private final int[] states;

    /**
     * Создать валидный собранный кубик
     * грани разместить по ордеру в енуме цветов
     * грань 0 -> цвет 0
     * грань 1 -> цвет 1
     * ...
     */
    public RubiksCube() {
		CubeColor[] colors = CubeColor.values();
        for (int i = 0; i < EDGES_COUNT; i++) edges[i] = new Edge(colors[i]);

		states = new int[Permutations.G_SIZE + 1];
        for (int i = 1; i < Permutations.G_SIZE + 1; i++)  states[i] = i;
    }

	public Edge[] getEdges() {
		for (int i = 0; i < EDGES_COUNT; i++) edges[Permutations.CURR_EDGES_ORDER[i]].setParts(buildEdge(i));
		return edges;
	}

	@Override
    public void front(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.F_CW : Permutations.F_CCW);
    }
    
	@Override
    public void back(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.B_CW : Permutations.B_CCW);
    }

    @Override
    public String toString() {
        return Arrays.toString(edges);
    }

	@Override
    public void left(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.L_CW : Permutations.L_CCW);
	}

	@Override
    public void right(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.R_CW : Permutations.R_CCW);
    }

	@Override
    public void up(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.U_CW : Permutations.U_CCW);
    }

    @Override
    public void down(RotateDirection direction) {
        applyCyclesToStates(direction == RotateDirection.CLOCKWISE ? Permutations.D_CW : Permutations.D_CCW);
    }

	private void applyCycleToState(int[] c) {
		final int cLen = c.length;
		int prev = states[c[cLen - 1]];
		for (int i = 0; i < cLen; i++) {
			int cur = states[c[i]];
			states[c[i]] = prev;
			prev = cur;
		}
	}

	private void applyCyclesToStates(int[][] cs) {
		for (int[] c : cs) applyCycleToState(c);
	}

	private static int toElemPos(int x, int y) {
		int pos = x + y * EDGE_SIZE + 1;
		return pos > CENTER_INDEX ? pos - 1 : pos;
	}

	private static boolean isCenter(int x, int y) {
		return x == 1 && y == 1;
	}

	private CubeColor centerColor(int edgeIndex) {
		return CubeColor.values()[Permutations.CURR_EDGES_ORDER[edgeIndex]];
	}

	private CubeColor elemColor(int edgeIndex, int pos) {
		int stateIndex = states[edgeIndex * FIXED_ELEM + pos];
		int front = (stateIndex - 1) / FIXED_ELEM;
		return CubeColor.values()[Permutations.CURR_EDGES_ORDER[front]];
	}

	private CubeColor[][] buildEdge(int edgeIndex) {
		CubeColor[][] newEdge = new CubeColor[EDGE_SIZE][EDGE_SIZE];
		for (int y = 0; y < EDGE_SIZE; y++) {
			for (int x = 0; x < EDGE_SIZE; x++)
				newEdge[y][x] = isCenter(x, y) ? centerColor(edgeIndex) : elemColor(edgeIndex, toElemPos(x, y));
		}
		return newEdge;
	}
}
