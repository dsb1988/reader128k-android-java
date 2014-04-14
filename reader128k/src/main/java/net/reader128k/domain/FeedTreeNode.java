package net.reader128k.domain;

import net.reader128k.interfaces.TreeNodeVisitor;
import org.codehaus.jackson.JsonNode;

public class FeedTreeNode extends TreeNode {
    public String favicon;
    public String feed_url;
    public boolean is_active;

    public void hydrate(JsonNode jsonNode) throws Exception {
        id = String.valueOf(jsonNode.get("id").getIntValue());
        favicon = jsonNode.get("favicon").getTextValue();
        feed_url = jsonNode.get("feed_url").getTextValue();
        is_active = jsonNode.get("is_active").getBooleanValue();
    }

    @Override
    public Object accept(TreeNodeVisitor visitor, Object... data) {
        return visitor.visit(this, data);
    }
}
