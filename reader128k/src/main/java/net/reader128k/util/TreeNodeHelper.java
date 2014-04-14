package net.reader128k.util;

import net.reader128k.domain.CategoryTreeNode;
import net.reader128k.domain.CircleTreeNode;
import net.reader128k.domain.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeHelper {
    public static ArrayList<TreeNode> getCircleNodes(List<TreeNode> from) {
        ArrayList<TreeNode> result = new ArrayList<TreeNode>();
        for (TreeNode node : from)
            if (node instanceof CircleTreeNode)
                result.add(node);

        return result;
    }

    public static ArrayList<TreeNode> getCategoryNodes(List<TreeNode> from) {
        ArrayList<TreeNode> result = new ArrayList<TreeNode>();
        for (TreeNode node : from)
            if (node instanceof CategoryTreeNode)
                result.add(node);

        return result;
    }
}
