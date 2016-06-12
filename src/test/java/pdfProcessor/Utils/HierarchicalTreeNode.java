package pdfProcessor.Utils;

import htmlParser.Text;

public class HierarchicalTreeNode {
    private int nodeIndex;
    private Text text;
    private HierarchicalTreeNode leftChild;
    private HierarchicalTreeNode rightChild;
    private static int currentNodeCount = 1;

    public HierarchicalTreeNode(Text text) {
        this.nodeIndex = currentNodeCount;
        this.text = text;
        this.leftChild = null;
        this.rightChild = null;
        currentNodeCount++;
    }

    public HierarchicalTreeNode(int nodeIndex, Text text) {
        this(text);
        this.nodeIndex = nodeIndex;
    }

    public HierarchicalTreeNode(HierarchicalTreeNode leftChild, HierarchicalTreeNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.nodeIndex = currentNodeCount;
        currentNodeCount++;
    }

    public HierarchicalTreeNode(
            int nodeIndex,
            HierarchicalTreeNode leftChild,
            HierarchicalTreeNode rightChild) {
        this(leftChild,rightChild);
        this.nodeIndex = nodeIndex;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public int isLeaf() {
        return this.text == null ? 0 : 1;
    }

}
