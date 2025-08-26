package io.github.xfacthd.framedblocks.api.model.quad;

record CuttingConfig(
        int forwardCoord,
        int parallelCoord,
        VertPair cutEdgeVerts,
        VertPair checkEdgeVerts,
        boolean invertParallelEdge,
        boolean swapCornerLengths,
        UvSrcVertSet uvVertsOne,
        UvSrcVertSet uvVertsTwo,
        boolean vAxis
) {
    record VertPair(int v1, int v2) { }

    record UvSrcVertSet(int posOne, int posTwo, int uvOne, int uvTwo) { }
}
