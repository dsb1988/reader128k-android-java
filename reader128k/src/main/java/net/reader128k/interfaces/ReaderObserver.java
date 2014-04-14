package net.reader128k.interfaces;

import net.reader128k.domain.*;

import java.util.List;

public interface ReaderObserver {
    public void onConnect();
    public void onUserInfo(User user);
    public void onTree(List<TreeNode> tree);
    public void onPosts(List<Post> posts);
    public void onLogin(LoginResponse response);
    public void onLogout(LogoutResponse response);
}
