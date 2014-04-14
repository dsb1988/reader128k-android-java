package net.reader128k.domain;

import net.reader128k.interfaces.TreeNodeVisitor;
import org.codehaus.jackson.JsonNode;

public class FriendTreeNode extends TreeNode {
    public String gravatar_prefix;

    public void hydrate(JsonNode jsonNode) throws Exception {
        id = String.valueOf(jsonNode.get("id").getIntValue());
        gravatar_prefix = jsonNode.get("gravatar_prefix").getTextValue();
    }

    @Override
    public Object accept(TreeNodeVisitor visitor, Object... data) {
        return visitor.visit(this, data);
    }
}
