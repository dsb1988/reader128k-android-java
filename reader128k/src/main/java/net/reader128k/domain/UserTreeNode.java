package net.reader128k.domain;

import net.reader128k.interfaces.TreeNodeVisitor;
import org.codehaus.jackson.JsonNode;

public class UserTreeNode extends TreeNode {
    public String favicon;

    public void hydrate(JsonNode jsonNode) throws Exception {
        favicon = jsonNode.get("favicon").getTextValue();
    }

    @Override
    public Object accept(TreeNodeVisitor visitor, Object... data) {
        return visitor.visit(this, data);
    }
}
