package net.reader128k.interfaces;

import net.reader128k.domain.*;

public interface TreeNodeVisitor {
    public Object visit(AllMyPostsTreeNode node, Object... userData);
    public Object visit(CategoryTreeNode node, Object... userData);
    public Object visit(CircleTreeNode node, Object... userData);
    public Object visit(UserTreeNode node, Object... userData);
    public Object visit(AllUsersTreeNode node, Object... userData);
    public Object visit(FriendTreeNode node, Object... userData);
    public Object visit(FeedTreeNode node, Object... userData);
}
