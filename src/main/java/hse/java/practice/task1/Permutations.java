package hse.java.practice.task1;

import java.util.Arrays;
import java.util.stream.*;
import java.util.stream.IntStream;

public class Permutations {
	static final int[] CURR_EDGES_ORDER = {2, 4, 3, 5, 0, 1};
	public static final int G_SIZE = 48;

	// Циклы перестановок для левой грани по часовой стрелке
	static final int[][] L_CW = {
		{1, 3, 8, 6},
		{2, 5, 7, 4},
		{33, 9, 41, 32},
		{36, 12, 44, 29},
		{38, 14, 46, 27}
	};

	// Циклы перестановок для передней грани по часовой стрелке
	static final int[][] F_CW = {
		{9, 11, 16, 14},
		{10, 13, 15, 12},
		{38, 17, 43, 8},
		{39, 20, 42, 5},
		{40, 22, 41, 3}
	};

	// Циклы перестановок для правой грани по часовой стрелке
	static final int[][] R_CW = {
		{17, 19, 24, 22},
		{18, 21, 23, 20},
		{48, 16, 40, 25},
		{45, 13, 37, 28},
		{43, 11, 35, 30}
	};

	// Циклы перестановок для задней грани по часовой стрелке
	static final int[][] B_CW = {
		{25, 27, 32, 30},
		{26, 29, 31, 28},
		{19, 33, 6, 48},
		{21, 34, 4, 47},
		{24, 35, 1, 46}
	};

	// Циклы перестановок для верхней грани по часовой стрелке
	static final int[][] U_CW = {
		{33, 35, 40, 38},
		{34, 37, 39, 36},
		{25, 17, 9, 1},
		{26, 18, 10, 2},
		{27, 19, 11, 3}
	};

	// Циклы перестановок для нижней грани по часовой стрелке
	static final int[][] D_CW = {
		{41, 43, 48, 46},
		{42, 45, 47, 44},
		{6, 14, 22, 30},
		{7, 15, 23, 31},
		{8, 16, 24, 32}
	};

	// То же самое, но против часовой
	static final int[][] U_CCW = reverseCycles(U_CW);
	static final int[][] D_CCW = reverseCycles(D_CW);
	static final int[][] L_CCW = reverseCycles(L_CW);
	static final int[][] R_CCW = reverseCycles(R_CW);
	static final int[][] F_CCW = reverseCycles(F_CW);
	static final int[][] B_CCW = reverseCycles(B_CW);

	private static int[][] reverseCycles(int[][] cs) {
	return Arrays.stream(cs)
			.map(c -> {
				final int cLen = c.length;
				int[] rev = new int[cLen];
				rev[0] = c[0];
				IntStream.range(1, cLen).forEach(i -> rev[i] = c[cLen - i]);
				return rev;
			})
			.toArray(int[][]::new);
	}
}
