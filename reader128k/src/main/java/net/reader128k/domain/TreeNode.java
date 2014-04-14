package net.reader128k.domain;

import android.os.Parcel;
import android.os.Parcelable;

import net.reader128k.interfaces.TreeNodeVisitor;
import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeNode implements Parcelable {
	public String id;
	public String name;
    public String type;
	public List<TreeNode> children = new ArrayList<TreeNode>();

	public static TreeNode deserialize(JsonNode jsonNode) throws Exception {
        TreeNode node;
        final String type = jsonNode.get("type").getTextValue();
		if (type.equals("all")) {
			JsonNode category = jsonNode.get("category");
            if (category != null && category.getTextValue().equals("friend"))
                node = new AllUsersTreeNode();
            else
                node = new AllMyPostsTreeNode();
		} else if (type.equals("user")) {
			node = new UserTreeNode();
		} else if (type.equals("circle"))
            node = new CircleTreeNode();
        else if (type.equals("category"))
            node = new CategoryTreeNode();
        else if (type.equals("friend"))
            node = new FriendTreeNode();
        else if (type.equals("feed"))
            node = new FeedTreeNode();
        else {
			throw new Exception("Unknown node type '" + type + "'.");
		}

		node.id = jsonNode.get("id").getTextValue();
		node.name = jsonNode.get("name").getTextValue();
        node.type = type;

		node.hydrate(jsonNode);

		JsonNode childrenNodes = jsonNode.get("children");
		if (childrenNodes != null)
			for (JsonNode childNode : childrenNodes)
				node.children.add(deserialize(childNode));

		return node;
	}

    public abstract void hydrate(JsonNode jsonNode) throws Exception;

    public abstract Object accept(TreeNodeVisitor visitor, Object... data);

    @Override
    public String toString() {
        return "id = " + id + ", name = " + name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeList(children);
    }

    public static final Creator<TreeNode> CREATOR = new Creator<TreeNode>() {

        @Override
        public TreeNode createFromParcel(Parcel parcel) {
            TreeNode result = null;
            try {
                result = createTreeNodeFromParcel(parcel);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        public TreeNode[] newArray(int i) {
            return new TreeNode[i];
        }
    };

    private static TreeNode createTreeNodeFromParcel(Parcel parcel) throws Exception {
        TreeNode result;
        String type = parcel.readString();

        if (type.equals("all")) {
            String category = parcel.readString();
            if (category.equals("friend"))
                result = new AllUsersTreeNode();
            else
                result = new AllMyPostsTreeNode();
        } else if (type.equals("user")) {
            result = new UserTreeNode();
        } else if (type.equals("circle"))
            result = new CircleTreeNode();
        else if (type.equals("category"))
            result = new CategoryTreeNode();
        else if (type.equals("friend"))
            result = new FriendTreeNode();
        else if (type.equals("feed"))
            result = new FeedTreeNode();
        else {
            throw new Exception("Unknown node type '" + type + "'.");
        }

        return result;
    }
}
