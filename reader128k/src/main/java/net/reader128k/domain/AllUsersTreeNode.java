package net.reader128k.domain;

import net.reader128k.interfaces.TreeNodeVisitor;
import org.codehaus.jackson.JsonNode;

public class AllUsersTreeNode extends TreeNode {
    public void hydrate(JsonNode jsonNode) throws Exception {
    }

    @Override
    public Object accept(TreeNodeVisitor visitor, Object... data) {
        return visitor.visit(this, data);
    }
}
